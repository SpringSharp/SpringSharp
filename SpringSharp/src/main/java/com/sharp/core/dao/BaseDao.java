package com.sharp.core.dao;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.RowSetDynaClass;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.transaction.annotation.Transactional;

import com.sharp.core.entity.BaseEntity;
import com.sharp.core.utils.DataFormat;
import com.sharp.core.utils.SpringUtil;

/**
 * ������: BaseDao
 * �����������ݿ��ѯ������
 * ����ʱ��: 2016-7-4 ����03:16:19
 * �����ˣ� ������
 * �汾�� 1.0
 * @param <T>
 * @since JDK 1.5
 */
@Transactional
public  class BaseDao<T> extends JdbcDaoSupport{

    private static final Logger logger = Logger.getLogger(BaseDao.class.getName()); 
	/**����������̬����*/
	final static protected int DAO_OPERATION_ADD = 0;
	/**ɾ��������̬����*/	
	final static protected int DAO_OPERATION_DEL = 1;
	/**���²�����̬����*/	
	final static protected int DAO_OPERATION_UPDATE = 2;
	/**��ѯ������̬����*/	
	final static protected int DAO_OPERATION_FIND = 3;

	/**��ǰDAO���������ݿ����*/
	protected String strTableName = null;
	
	protected BaseDao(){
	}
	
	protected BaseDao(String tableName) {
		strTableName = tableName;
	}
	protected JdbcTemplate jdbcTemplate = (JdbcTemplate) SpringUtil.getBean("jdbcTemplate");
	
	/**ʹ��squence����max id��ȡID�ı�־*/
	private boolean isUseMaxID = false;
	
	
	
	/**
	 * Ĭ����ͨ��sequence��ȡID�������Ҫ��ȡMax id����ʹ�ô˷���
	 * */
	public void setUseMaxID(){
		this.isUseMaxID = true;
	}

	/**
	 * ���ݿ�����������������ID����������set����ȷ����ֵ��
	 * @param dataEntity ��Ҫ���������ݿ���Ӧ��Data Entity��ʵ��
	 * @param ��
	 * @return �²�����ID
	 * @throws Exception 
	 */		
	public long add(BaseEntity dataEntity) throws Exception{
		long lId = -1;
			
		//���ÿ�ֵ��DataEntity����ʹ�ñ�,ʹ��setPrepareStatementByDataEntity��ID���и�ֵ
		dataEntity.setlId(-1);
		StringBuffer buffer = new StringBuffer();
		buffer.append("INSERT INTO "+ strTableName+" (\n");
		String[] buffers = getAllFieldNameBuffer(dataEntity,DAO_OPERATION_ADD);
		buffer.append(buffers[0]);
		buffer.append("\n) "+ "VALUES (\n");				
		buffer.append(buffers[1] +") \n");
		
		String sql = buffer.toString();
		logger.debug(sql);		
		Object[ ] params=new Object[ ]{ };
		params = setPrepareStatementByDataEntity(dataEntity,DAO_OPERATION_ADD,buffers[0].toString());
		lId = jdbcTemplate.update(sql, params);
		return lId;
	}
	/**
	 * �����ݿ��и���id����ɾ��һ����¼
	 * ʹ��ʱ��ȷ�����߼�ɾ����������ɾ������
	 * */
	public void deletePhysically(long lId) throws Exception{
		String sql = "delete from "+strTableName +" where lId = "+ lId;
		logger.debug(sql);
		jdbcTemplate.update(sql);
	}
	/**
	 * ���ݿ���²�����
	 * @param dataEntity ��Ҫ�����µ����ݿ���Ӧ��Data Entity��ʵ��
	 * @param ��
	 * @return
	 * @throws Exception
	 */			
	public void update(BaseEntity dataEntity) throws Exception{
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("UPDATE "+ strTableName+" SET \n");
		String[] buffers = getAllFieldNameBuffer(dataEntity,DAO_OPERATION_UPDATE);
		buffer.append(buffers[0]);
		buffer.append(" WHERE lId = "+ dataEntity.getlId());
		String sql = buffer.toString();
		Object[ ] params=new Object[ ]{ };
		params = setPrepareStatementByDataEntity(dataEntity,DAO_OPERATION_UPDATE,buffers[0].toString());
		jdbcTemplate.update(sql, params);
	}	



	/**
	 * ���ݿ���Ҳ���
	 * @param lId������
	 * @param className ��Ҫ���ҵ����ݿ���Ӧ��Data Entity������
	 * @return
	 * @throws Exception
	 */		
	public BaseEntity findByID(long lId, Class<?> className)  throws Exception{
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT * FROM " + strTableName + " WHERE lId = " + lId);
		String sql = buffer.toString();
		logger.debug(sql);
		Map<String, Object> map = new HashMap<String, Object>();
		map = this.getJdbcTemplate().queryForMap(sql);
		BaseEntity res = getDataEntityFromResultSet(className,map);

		return res;
	}
	
	/**
	 * ���ݿ���Ҳ���
	 * @param conditionInfo ��ѯ�����������set��ֵ������Ϊ���ֶ�Ϊ��ѯ��������ƥ�䡡����
	 * @param className ��Ҫ���ҵ����ݿ���Ӧ��Data Entity������
	 * @return
	 * @throws Exception
	 */			
	public List<Map<String, Object>> findByCondition(BaseEntity conditionInfo) throws Exception{
		return findByCondition(conditionInfo,null);
	}


	/**
	 * ���ݿ���Ҳ���
	 * @param conditionInfo ��ѯ�����������set��ֵ������Ϊ���ֶ�Ϊ��ѯ��������ƥ�䡡����
	 * @param className ��Ҫ���ҵ����ݿ���Ӧ��Data Entity������
	 * @param orderByString order by SQL��䣬�ֹ����
	 * @return
	 * @throws Exception
	 */			
    public List<Map<String, Object>> findByCondition(BaseEntity conditionInfo,String orderByString) throws Exception{
	    
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT * FROM " + strTableName);
		buffer.append("\n WHERE ");
		String strs[] = this.getAllFieldNameBuffer(conditionInfo, DAO_OPERATION_FIND);
		buffer.append(strs[0]);
		
		String sql = buffer.toString();
		if(orderByString != null){
			sql += " ";
			sql += orderByString;
		}
		
		Object[ ] params=new Object[ ]{ };
		params = setPrepareStatementByDataEntity(conditionInfo,DAO_OPERATION_FIND, strs[0]);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,params);
		return  list;		
	}
	

	
   /**
	* ���ݲ�������(add ���� update)��ȡ���е��ֶ�����
	* @return��String[]
	* @param��String1:
	* @param String2��update������ʹ�ø÷���ֵ
	* @throws Exception
	* @deprecated
	*/

	public String[] getAllFieldNameBuffer(Class<?> className, int operationType) throws Exception{
		StringBuffer buffer1 = new StringBuffer();//����
		StringBuffer buffer2 = new StringBuffer();//�ʺ�
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(className);
		} catch (IntrospectionException e) {
			throw new Exception("Java Bean.��ʡ�쳣����",e);			
		}		
		PropertyDescriptor[] properties =
			beanInfo.getPropertyDescriptors();
		for (int i = 0; i < properties.length; i++) {
			String fieldName = properties[i].getName();
			if(fieldName.compareToIgnoreCase("class")==0)//ignore field "class" of super class
				continue;

			if(operationType == DAO_OPERATION_ADD){		
				buffer1.append(fieldName+",");
			}else if(operationType == DAO_OPERATION_UPDATE){
				if(fieldName.compareToIgnoreCase("lId") == 0)//���²�������id���д���
					continue;
				buffer1.append(fieldName+" = ?,");				
			}else{
				throw new Exception("DAO getAllFieldNameBuffer,����Ĳ�������",null);
			}
			
			buffer2.append("?,");			
		}
		String strBuffer1 = buffer1.toString();
		String strBuffer2 = buffer2.toString();		
		
		strBuffer1 = strBuffer1.substring(0,(strBuffer1.length()-1));
		strBuffer2 = strBuffer2.substring(0,(strBuffer2.length()-1));
		
		String[] strs = {strBuffer1,strBuffer2};
		return strs;
		
	}

   /**
 	* ���ݲ�������(add ���� update)��Data Entity�е�Hashtable�л�ȡ���б�ʹ�ù����ֶ�����
 	* @return��String[]
 	* @param��String1:
 	* @param String2��update������ʹ�ø÷���ֵ
 	* @throws Exception
 	**/

	public String[] getAllFieldNameBuffer(BaseEntity dataEntity, int operationType) throws Exception{
		StringBuffer buffer1 = new StringBuffer();//����
		StringBuffer buffer2 = new StringBuffer();//�ʺ�

		HashMap<?, ?> allFields = dataEntity.gainAllUsedFieldsAndValue();
		Set<?> allFieldNames = allFields.keySet();
		
		Iterator<?> it = allFieldNames.iterator();
		while(it.hasNext()) {
			String fieldName = (String) it.next(); 
			//logger.debug("Used Field Name: " + fieldName);

			if(operationType == DAO_OPERATION_ADD){		
				buffer1.append(fieldName+",");
			}else if(operationType == DAO_OPERATION_UPDATE || operationType == DAO_OPERATION_FIND){
				if(operationType == DAO_OPERATION_UPDATE && fieldName.compareToIgnoreCase("lId") == 0)//���²�������id���д���
					continue;
				//do not modify any blank at below codes because "= ?," is match condition for split field name
				if(operationType == DAO_OPERATION_FIND)
					buffer1.append(fieldName+" = ? AND ");
				else
					buffer1.append(fieldName+" = ?,");
			}else{
				throw new Exception("DAO getAllFieldNameBuffer,����Ĳ�������",null);
			}
			
			buffer2.append("?,");			
		}
		String strBuffer1 = buffer1.toString();
		String strBuffer2 = buffer2.toString();	
		
		if(operationType == DAO_OPERATION_FIND)//cut last "and"
			strBuffer1 = strBuffer1.substring(0,(strBuffer1.length()-4));
		else//cut last ","
			strBuffer1 = strBuffer1.substring(0,(strBuffer1.length()-1));
		strBuffer2 = strBuffer2.substring(0,(strBuffer2.length()-1));
		
		String[] strs = {strBuffer1,strBuffer2};
		return strs;
		
	}

	/**
 	 * ���ݲ�������(add ���� update)��Data Entity�е�Hashtable�л�ȡ���б�ʹ�ù����ֶ�����
 	 * @return��String[]
 	 * @param��String1:
 	 * @param String2��update������ʹ�ø÷���ֵ
 	 * @throws Exception
 	 * */
	public String[] getAllFieldNameAndValueBuffer(BaseEntity dataEntity, int operationType) throws Exception{
		StringBuffer bufferName = new StringBuffer();	//����
		StringBuffer bufferValue = new StringBuffer();	//ֵ

		HashMap<?, ?> allFields = dataEntity.gainAllUsedFieldsAndValue();
		Set<?> allFieldNames = allFields.keySet();
		Set<?> allFieldValues = allFields.entrySet();
		
		Iterator<?> itName = allFieldNames.iterator();
		Iterator<?> itValue = allFieldValues.iterator();
		while(itName.hasNext() && itValue.hasNext()) {
			String fieldName = (String) itName.next();
			bufferName.append(fieldName+" = ? AND ");
		}
		String strBufferName = bufferName.toString();
		String strBufferValue = bufferValue.toString();		

		if(operationType == DAO_OPERATION_FIND)//cut last "and"
			strBufferName = strBufferName.substring(0,(strBufferName.length()-4));
		else//cut last ","
			strBufferName = strBufferName.substring(0,(strBufferName.length()-1));
		strBufferValue = strBufferValue.substring(0,(strBufferValue.length()-1));
		//logger.debug("----afer cut-------");

		String[] strs = {strBufferName,strBufferValue};
		return strs;
	}

	
	/**
	 * ����DataEntity�е���Ϣ��PrepareStatement����������
	 * @param ITreasuryBaseDataEntity��������Ҫ���ݸ�PrepareStatement���ݵ�Data Entity
	 * @param operationType          ��������
	 * @param fieldNames���������������ö��Ÿ����������ֶ�����
	 * @return ��������add����������IDֵ�����򷵻�-1
	 * @throws Exception
	 */
	public Object[ ] setPrepareStatementByDataEntity(BaseEntity dataEntity, int operationType, String fieldNames) throws Exception{

		String[] fieldNameArray = null;
		Object[ ] params=new Object[ ]{ };
		
		//fieldNameArray = fieldNames.split(",");
		if(operationType == DAO_OPERATION_FIND){
			fieldNameArray = DataFormat.splitString(fieldNames, "AND");
		}
		else{	
			fieldNameArray = DataFormat.splitString(fieldNames, ",");
		}
		
		for (int i = 0; i < fieldNameArray.length; i++) {
			String fieldName = null;

			fieldName = (fieldNameArray[i]).trim();
							
			if(operationType == DAO_OPERATION_UPDATE || operationType == DAO_OPERATION_FIND){//cut " = ?"
				fieldName = fieldName.substring(0,fieldName.length()-4);
			}

			//���²�������ID���д���	
			if(operationType == DAO_OPERATION_UPDATE && fieldName.compareToIgnoreCase("lId") == 0)//���²�������id���д���
					continue;				
			try {
				HashMap<?, ?> allFieldsAndValues = dataEntity.gainAllUsedFieldsAndValue();
				Object resValue = allFieldsAndValues.get(fieldName);
				
				if(resValue instanceof Long){
					long value = ((Long)(resValue)).longValue();
					if(operationType == DAO_OPERATION_ADD && fieldName.compareToIgnoreCase("lId") == 0){
						if(!isUseMaxID){
							value = geSequenceID();
						}else{ 
							value = getMaxID();
						}
						params = addArray(params,value);
					}else{
						params = addArray(params,value);
					}
				}else if(resValue instanceof Double){
					double value = ((Double)(resValue)).doubleValue();				
					params = addArray(params,value);
				}else if(resValue instanceof String){
					String value = (String) resValue;
					params = addArray(params,value);
				}else if(resValue instanceof Timestamp){
					Timestamp value = (Timestamp)resValue;
					if(value.equals(BaseEntity.getNullTimeStamp())){
						value = null;
					}
					params = addArray(params,value);
				}
				else{
					throw new Exception("Debug:setPrepareStatementByDataEntity���Ͳ�ƥ��",null);
				}
				logger.debug(fieldName+" value is: "+resValue);

			} catch (SQLException e) {
				throw new Exception("���ݿ��쳣����",e);
			}

		}	
		logger.debug("-----end of setPrepareStatementByDataEntity--------");
		return params;
	
	}	
	/**
	 * ��ResultSet�л�ȡ��ѯ�����
	 * @param className ��Ҫ���ҵ����ݿ���Ӧ��Data Entity������
	 * @param ��
	 * @return
	 * @throws Exception
	 */	
	public BaseEntity getDataEntityFromResultSet(Class<?> dataEntityClass,Map<String,Object> map) throws Exception{
		BaseEntity res = null;
		try {
			try {
				logger.debug("--------getDataEntityFromResultSet----------");

				res = (BaseEntity) dataEntityClass.newInstance();
			} catch (InstantiationException e) {
				throw new Exception("Data Entityʵ�����쳣����",e);					
			} catch (IllegalAccessException e) {
				throw new Exception("Data Entityʵ�����Ƿ������쳣����",e);
			}

			if (map!=null&&map.size()>0) {
				BeanInfo beanInfo = Introspector.getBeanInfo(dataEntityClass);
				PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();
				for (int i = 0; i < properties.length; i++) {
					Class<?> p = properties[i].getPropertyType();
					String fieldName = properties[i].getName();
					fieldName = getFieldName(p, fieldName);					
					Method writeMethod = properties[i].getWriteMethod();
					if(writeMethod == null)
						continue;

					Object valueObj = null;
					try{				
					
					valueObj = map.get(fieldName.toLowerCase());
					if(valueObj == null)//������DataEntity�ж����ж�Ӧ�����ݿ����û�еı���
						continue;
					}catch(IllegalArgumentException e){
						//������DataEntity�ж����ж�Ӧ�����ݿ����û�еı���
						continue;
					}

					if(valueObj instanceof BigDecimal){
						if(p.getName().compareToIgnoreCase("long") == 0){
							valueObj = new Long(valueObj.toString());
						}else if(p.getName().compareToIgnoreCase("double") == 0){
							valueObj = new Double(valueObj.toString());							
						}
					}else if(valueObj instanceof Timestamp || valueObj instanceof String){
					}else if (valueObj instanceof Date)
                    {
                    }
					else{
						continue;
					}

					Object[] args = {valueObj};					
					try {				
						writeMethod.invoke(res, args);
					} catch (IllegalAccessException e) {
						throw new Exception("Data Entityʵ�����Ƿ��쳣����",e);
					} catch (InvocationTargetException e) {
						throw new Exception("Data Entity����Ŀ���쳣����",e);
					} catch(Exception e){
						e.printStackTrace();
					}
				}
			}

		} catch (SQLException e) {
			throw new Exception("���ݿ��쳣����",e);
		} catch (IntrospectionException e) {
			throw new Exception("Java Bean.��ʡ�쳣����",e);
		}
		return res;
	}
	
	
    /**
     * �������ƣ�getSuperClassGenricType
     * ����������ͨ�����䣬��ȡ����classʱ��������ʱ�ķ��Ͳ��������ͣ����޷��ҵ�����Object.class
     * �����ˣ� ������
     * ����ʱ�䣺2016��7��13�� ����4:56:06
     * @param clazz
     * @param index
     * @return
     * @since JDK 1.5
     */
    @SuppressWarnings("unchecked")
    public static Class<Object> getSuperClassGenricType(final Class<?> clazz, final int index) {  
        
        //���ر�ʾ�� Class ����ʾ��ʵ�壨�ࡢ�ӿڡ��������ͻ� void����ֱ�ӳ���� Type��  
        Type genType = clazz.getGenericSuperclass();  
  
        if (!(genType instanceof ParameterizedType)) {  
           return Object.class;  
        }  
        //���ر�ʾ������ʵ�����Ͳ����� Type ��������顣  
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();  
  
        if (index >= params.length || index < 0) {  
                     return Object.class;  
        }  
        if (!(params[index] instanceof Class)) {  
              return Object.class;  
        }  
  
        return  (Class<Object>) params[index];  
    } 
   /**
    * ����sequence�����������ȡ��һ�����õ�IDֵ
    * sequence��������: seq_+tablename������,SEC_Remark���sequenceΪSEQ_SEC_Remark
    * @param
    * @param
    * @return ��һ��ID
    * @throws Exception
    */
	protected long geSequenceID()  throws Exception{
			/**
			 * �˷���ֻ����DAO�б����ã��������´������ݿ���Դ�����Ҳ����Ҫ �ر����ݿ���Դ
			 */
			long lId = -1;
			String strSeqName = "SEQ_" + strTableName;
			String sql = "SELECT " + strSeqName + ".nextval nextid from dual";
			try {
				lId = this.getJdbcTemplate().queryForLong(sql);
			} catch (Exception e) {
				e.printStackTrace();
				new Exception("���ݿ��ȡID���쳣",e);
			}
			return lId;		
	}
	
	private long getMaxID()  throws Exception{
		long lId = -1;
		StringBuffer sql = new StringBuffer();
		sql.append("select nvl(max(lId)+1,1) lId from " + strTableName);
		try {
			
			lId = jdbcTemplate.queryForLong(sql.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lId;		
	}

	/**
	 * �����������ͼ��Ƿ���ǰ׺,ȷ�����ֶ���DataEntity�е�����
	 * ע:�ֶ�IDû��ǰ׺
	 */
	private String getFieldName(Class<?> propertyType, String fieldName) {
		if(fieldName.equalsIgnoreCase("lId")){
			String prefixName = BaseEntity.DataTypeName.getPrefixByDataType(propertyType.getName());						
			logger.debug("-------prefixName: "+prefixName);
		}
		return fieldName;
	}		

	/**
	 * �������ƣ�add
	 * �����������������������ֵ
	 * �����ˣ� ������
	 * ����ʱ�䣺2016-7-5 ����02:34:01
	 * @param addArray
	 * @param param
	 * @return
	 * @since JDK 1.5
	 */
	private Object[]  addArray( Object[] array, Object param)
	{
		//Object[ ] params=new Object[ ]{ };
		Object[] temp = array;
	    array = new Object[temp.length + 1];
	    for (int i = 0; i < temp.length; i++)
	    {
	        array[i] = temp[i];
	    }
	    array[array.length - 1] = param;
	    return array;
	}

	
    /**
     * �������ƣ�isDuplicate
     * ����������������ݿ����Ƿ����йؼ����ظ��ļ�¼
     * �����ˣ� ������
     * ����ʱ�䣺2015-12-14 ����07:40:18
     * @param info
     * @param arrFieldsNames
     * @description ArrayList arr=new ArrayList();  arr.add("usedField")
     * @return
     * @throws SecuritiesException
     * @since JDK 1.5
     */
    public boolean isDuplicate(BaseEntity info,ArrayList<?> arrFieldsNames) throws Exception{
        boolean isDuplicate=false;
        StringBuffer bufWhere=new StringBuffer();
        String[] dataType = {"double","long","java.lang.String","java.sql.Timestamp"};          //֧�ֵ���������

        BeanInfo beanInfo = null;
        try{
            logger.debug(info.getClass().getName());
            beanInfo = Introspector.getBeanInfo(info.getClass());   
            PropertyDescriptor[] p = beanInfo.getPropertyDescriptors();
            for (int n=0;n<p.length;n++){
            
                logger.debug("----p["+n+"].getName() = '"+p[n].getName()+"'");
                logger.debug("----String.valueOf(p["+n+"].getReadMethod().invoke(this,null) = "+String.valueOf(p[n].getReadMethod().invoke(info,new Object[]{null})));
                if (p[n].getName().compareToIgnoreCase("class")==0) continue;
                
                for(int i=0;i<arrFieldsNames.size();i++){
                    logger.debug("arrFieldsNames.get("+i+")='"+arrFieldsNames.get(i)+"'");
                    if (p[n].getName().compareToIgnoreCase((String)arrFieldsNames.get(i))==0){
                        try{
                            if (p[n].getReadMethod()!=null){
                                String strValue = (p[n].getReadMethod().invoke(info,new Object[]{null})==null)?"":String.valueOf(p[n].getReadMethod().invoke(info,new Object[]{null}));
                                String strReturnType = p[n].getReadMethod().getReturnType().getName();
                                
                                if( strReturnType.equals(dataType[0]) || strReturnType.equals(dataType[1])){//parameter type is double
                                    bufWhere.append(p[n].getName()+" = "+strValue +" and ");
                                }                           
                                else if(strReturnType.equals(dataType[2])){         //parameter type is String
                                    bufWhere.append(p[n].getName()+" = '"+strValue +"' and ");
                                }
                                else{
                                    throw new Exception("��֧��"+strReturnType+"�����ֶε��ظ���¼�Ƚ�");
                                }                           
                            }                   
                        }
                        catch (IllegalAccessException e){               
                            throw new Exception("����ظ���¼ʱ����",e);
                        }
                        catch(InvocationTargetException e){
                            throw new Exception("����ظ���¼ʱ����",e);
                        }           
                    }           
                }       
            }
            if (bufWhere.length()>0){
                String where=bufWhere.substring(0,(bufWhere.length()-4));
                String sql="select * from "+strTableName+" where "+where;
                logger.debug("���SQL: = "+sql);
        		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        		list = this.getJdbcTemplate().queryForList(sql);
                if(list!=null && list.size()>0){
                    isDuplicate=true;
                }
            }
        
        }   
        catch(Exception ex){
            ex.printStackTrace();
            throw new Exception("��ѯҵ���Ƿ��ظ�����",ex);
        }       
        return isDuplicate;
    }


		
}
