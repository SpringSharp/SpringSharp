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
 * 类名称: BaseDao
 * 类描述：数据库查询基础类
 * 创建时间: 2016-7-4 下午03:16:19
 * 创建人： 邢凌霄
 * 版本： 1.0
 * @param <T>
 * @since JDK 1.5
 */
@Transactional
public  class BaseDao<T> extends JdbcDaoSupport{

    private static final Logger logger = Logger.getLogger(BaseDao.class.getName()); 
	/**新增操作静态类型*/
	final static protected int DAO_OPERATION_ADD = 0;
	/**删除操作静态类型*/	
	final static protected int DAO_OPERATION_DEL = 1;
	/**更新操作静态类型*/	
	final static protected int DAO_OPERATION_UPDATE = 2;
	/**查询操作静态类型*/	
	final static protected int DAO_OPERATION_FIND = 3;

	/**当前DAO操作的数据库表名*/
	protected String strTableName = null;
	
	protected BaseDao(){
	}
	
	protected BaseDao(String tableName) {
		strTableName = tableName;
	}
	protected JdbcTemplate jdbcTemplate = (JdbcTemplate) SpringUtil.getBean("jdbcTemplate");
	
	/**使用squence还是max id获取ID的标志*/
	private boolean isUseMaxID = false;
	
	
	
	/**
	 * 默认是通过sequence获取ID，如果需要获取Max id，请使用此方法
	 * */
	public void setUseMaxID(){
		this.isUseMaxID = true;
	}

	/**
	 * 数据库新增操作，新增的ID必须在子类set入正确的数值　
	 * @param dataEntity 需要被插入数据库表对应的Data Entity的实例
	 * @param 　
	 * @return 新产生的ID
	 * @throws Exception 
	 */		
	public long add(BaseEntity dataEntity) throws Exception{
		long lId = -1;
			
		//设置空值到DataEntity的已使用表,使得setPrepareStatementByDataEntity会ID进行付值
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
	 * 从数据库中根据id物理删除一条记录
	 * 使用时请确定是逻辑删除还是物理删除！！
	 * */
	public void deletePhysically(long lId) throws Exception{
		String sql = "delete from "+strTableName +" where lId = "+ lId;
		logger.debug(sql);
		jdbcTemplate.update(sql);
	}
	/**
	 * 数据库更新操作　
	 * @param dataEntity 需要被更新的数据库表对应的Data Entity的实例
	 * @param 　
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
	 * 数据库查找操作
	 * @param lId　　　
	 * @param className 需要查找的数据库表对应的Data Entity的类名
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
	 * 数据库查找操作
	 * @param conditionInfo 查询条件，如果被set过值，则认为该字段为查询条件进行匹配　　　
	 * @param className 需要查找的数据库表对应的Data Entity的类名
	 * @return
	 * @throws Exception
	 */			
	public List<Map<String, Object>> findByCondition(BaseEntity conditionInfo) throws Exception{
		return findByCondition(conditionInfo,null);
	}


	/**
	 * 数据库查找操作
	 * @param conditionInfo 查询条件，如果被set过值，则认为该字段为查询条件进行匹配　　　
	 * @param className 需要查找的数据库表对应的Data Entity的类名
	 * @param orderByString order by SQL语句，手工添加
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
	* 根据操作类型(add 还是 update)获取所有的字段名称
	* @return　String[]
	* @param　String1:
	* @param String2　update操作不使用该返回值
	* @throws Exception
	* @deprecated
	*/

	public String[] getAllFieldNameBuffer(Class<?> className, int operationType) throws Exception{
		StringBuffer buffer1 = new StringBuffer();//名称
		StringBuffer buffer2 = new StringBuffer();//问号
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(className);
		} catch (IntrospectionException e) {
			throw new Exception("Java Bean.内省异常发生",e);			
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
				if(fieldName.compareToIgnoreCase("lId") == 0)//更新操作不对id进行处理
					continue;
				buffer1.append(fieldName+" = ?,");				
			}else{
				throw new Exception("DAO getAllFieldNameBuffer,错误的操作类型",null);
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
 	* 根据操作类型(add 还是 update)从Data Entity中的Hashtable中获取所有被使用过的字段名称
 	* @return　String[]
 	* @param　String1:
 	* @param String2　update操作不使用该返回值
 	* @throws Exception
 	**/

	public String[] getAllFieldNameBuffer(BaseEntity dataEntity, int operationType) throws Exception{
		StringBuffer buffer1 = new StringBuffer();//名称
		StringBuffer buffer2 = new StringBuffer();//问号

		HashMap<?, ?> allFields = dataEntity.gainAllUsedFieldsAndValue();
		Set<?> allFieldNames = allFields.keySet();
		
		Iterator<?> it = allFieldNames.iterator();
		while(it.hasNext()) {
			String fieldName = (String) it.next(); 
			//logger.debug("Used Field Name: " + fieldName);

			if(operationType == DAO_OPERATION_ADD){		
				buffer1.append(fieldName+",");
			}else if(operationType == DAO_OPERATION_UPDATE || operationType == DAO_OPERATION_FIND){
				if(operationType == DAO_OPERATION_UPDATE && fieldName.compareToIgnoreCase("lId") == 0)//更新操作不对id进行处理
					continue;
				//do not modify any blank at below codes because "= ?," is match condition for split field name
				if(operationType == DAO_OPERATION_FIND)
					buffer1.append(fieldName+" = ? AND ");
				else
					buffer1.append(fieldName+" = ?,");
			}else{
				throw new Exception("DAO getAllFieldNameBuffer,错误的操作类型",null);
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
 	 * 根据操作类型(add 还是 update)从Data Entity中的Hashtable中获取所有被使用过的字段名称
 	 * @return　String[]
 	 * @param　String1:
 	 * @param String2　update操作不使用该返回值
 	 * @throws Exception
 	 * */
	public String[] getAllFieldNameAndValueBuffer(BaseEntity dataEntity, int operationType) throws Exception{
		StringBuffer bufferName = new StringBuffer();	//名称
		StringBuffer bufferValue = new StringBuffer();	//值

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
	 * 根据DataEntity中的信息向PrepareStatement中设置数据
	 * @param ITreasuryBaseDataEntity　　　需要传递给PrepareStatement数据的Data Entity
	 * @param operationType          操作类型
	 * @param fieldNames　　　　　　　用逗号隔开的所有字段名称
	 * @return 如果是针对add操作，返回ID值，否则返回-1
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

			//更新操作不对ID进行处理	
			if(operationType == DAO_OPERATION_UPDATE && fieldName.compareToIgnoreCase("lId") == 0)//更新操作不对id进行处理
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
					throw new Exception("Debug:setPrepareStatementByDataEntity类型不匹配",null);
				}
				logger.debug(fieldName+" value is: "+resValue);

			} catch (SQLException e) {
				throw new Exception("数据库异常发生",e);
			}

		}	
		logger.debug("-----end of setPrepareStatementByDataEntity--------");
		return params;
	
	}	
	/**
	 * 从ResultSet中获取查询结果　
	 * @param className 需要查找的数据库表对应的Data Entity的类名
	 * @param 　
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
				throw new Exception("Data Entity实例化异常发生",e);					
			} catch (IllegalAccessException e) {
				throw new Exception("Data Entity实例化非法访问异常发生",e);
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
					if(valueObj == null)//可能在DataEntity中定义有对应的数据库表中没有的变量
						continue;
					}catch(IllegalArgumentException e){
						//可能在DataEntity中定义有对应的数据库表中没有的变量
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
						throw new Exception("Data Entity实例化非法异常发生",e);
					} catch (InvocationTargetException e) {
						throw new Exception("Data Entity调用目标异常发生",e);
					} catch(Exception e){
						e.printStackTrace();
					}
				}
			}

		} catch (SQLException e) {
			throw new Exception("数据库异常发生",e);
		} catch (IntrospectionException e) {
			throw new Exception("Java Bean.内省异常发生",e);
		}
		return res;
	}
	
	
    /**
     * 方法名称：getSuperClassGenricType
     * 方法描述：通过反射，获取定义class时声明父类时的泛型参数的类型，如无法找到返回Object.class
     * 创建人： 邢凌霄
     * 创建时间：2016年7月13日 下午4:56:06
     * @param clazz
     * @param index
     * @return
     * @since JDK 1.5
     */
    @SuppressWarnings("unchecked")
    public static Class<Object> getSuperClassGenricType(final Class<?> clazz, final int index) {  
        
        //返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。  
        Type genType = clazz.getGenericSuperclass();  
  
        if (!(genType instanceof ParameterizedType)) {  
           return Object.class;  
        }  
        //返回表示此类型实际类型参数的 Type 对象的数组。  
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
    * 根据sequence的命名规则获取下一个可用的ID值
    * sequence命名规则: seq_+tablename，例如,SEC_Remark表的sequence为SEQ_SEC_Remark
    * @param
    * @param
    * @return 下一个ID
    * @throws Exception
    */
	protected long geSequenceID()  throws Exception{
			/**
			 * 此方法只能在DAO中被调用，即不重新创建数据库资源，因此也不需要 关闭数据库资源
			 */
			long lId = -1;
			String strSeqName = "SEQ_" + strTableName;
			String sql = "SELECT " + strSeqName + ".nextval nextid from dual";
			try {
				lId = this.getJdbcTemplate().queryForLong(sql);
			} catch (Exception e) {
				e.printStackTrace();
				new Exception("数据库获取ID产异常",e);
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
	 * 根据数据类型及是否有前缀,确定改字段在DataEntity中的名称
	 * 注:字段ID没有前缀
	 */
	private String getFieldName(Class<?> propertyType, String fieldName) {
		if(fieldName.equalsIgnoreCase("lId")){
			String prefixName = BaseEntity.DataTypeName.getPrefixByDataType(propertyType.getName());						
			logger.debug("-------prefixName: "+prefixName);
		}
		return fieldName;
	}		

	/**
	 * 方法名称：add
	 * 方法描述：往数组里面添加值
	 * 创建人： 邢凌霄
	 * 创建时间：2016-7-5 下午02:34:01
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
     * 方法名称：isDuplicate
     * 方法描述：检查数据库中是否已有关键字重复的记录
     * 创建人： 邢凌霄
     * 创建时间：2015-12-14 下午07:40:18
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
        String[] dataType = {"double","long","java.lang.String","java.sql.Timestamp"};          //支持的数据类型

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
                                    throw new Exception("不支持"+strReturnType+"类型字段的重复记录比较");
                                }                           
                            }                   
                        }
                        catch (IllegalAccessException e){               
                            throw new Exception("检查重复记录时出错",e);
                        }
                        catch(InvocationTargetException e){
                            throw new Exception("检查重复记录时出错",e);
                        }           
                    }           
                }       
            }
            if (bufWhere.length()>0){
                String where=bufWhere.substring(0,(bufWhere.length()-4));
                String sql="select * from "+strTableName+" where "+where;
                logger.debug("输出SQL: = "+sql);
        		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        		list = this.getJdbcTemplate().queryForList(sql);
                if(list!=null && list.size()>0){
                    isDuplicate=true;
                }
            }
        
        }   
        catch(Exception ex){
            ex.printStackTrace();
            throw new Exception("查询业务是否重复出错",ex);
        }       
        return isDuplicate;
    }


		
}
