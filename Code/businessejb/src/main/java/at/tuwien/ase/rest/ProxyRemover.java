package at.tuwien.ase.rest;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.collection.internal.PersistentSet;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.pojo.javassist.SerializableProxy;

import at.tuwien.ase.model.User;
public class ProxyRemover<T> {
	 public static <T> T cleanFromProxies(Session session,T value) throws Exception {
		 
		 session.setFlushMode(FlushMode.MANUAL);//dont let dirty checking  make mess
		 T result =cleanFromProxies(value);
 
	        return result;
	    }
	 private static <T> T cleanFromProxies(T value) throws Exception {
		 
	        T result = unproxyObject(value);
	        cleanFromProxies(result, new ArrayList<Object>());
	        return result;
	    }


	    private static void cleanFromProxies(Object value, List<Object> handledObjects) throws Exception {
	 
	        if ((value != null) && (!isProxy(value)) &&   !handledObjects.contains(value)) {
	            handledObjects.add(value);
	            if (value instanceof Iterable) {
	                for (Object item : (Iterable<?>) value) {
	                    cleanFromProxies(item, handledObjects);
	                }
	            } else if (value.getClass().isArray()) {
	                for (Object item : (Object[]) value) {
	                    cleanFromProxies(item, handledObjects);
	                }
	            }
	            BeanInfo beanInfo = null;
	            try {
	                beanInfo = Introspector.getBeanInfo(value.getClass());
	            } catch (IntrospectionException e) {
	               throw e;
	            }
	            if (beanInfo != null) {
	                for (PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
	                    try {
	                        if ((property.getWriteMethod() != null) && (property.getReadMethod() != null)) {
	                            Object fieldValue = property.getReadMethod().invoke(value);
	                            if (isProxy(fieldValue)) {
	                                fieldValue = unproxyObject(fieldValue);
	                                property.getWriteMethod().invoke(value, fieldValue);
	                            }
	                            cleanFromProxies(fieldValue, handledObjects);
	                        }
	                    } catch (Exception e) {
	                      	throw e;
	                    }
	                }
	            }
	        }
	    }

	    public static boolean isProxy(Object value) {
	        if (value == null) {
	            return false;
	        }
	        if ((value instanceof HibernateProxy) || (value instanceof PersistentCollection)) {
	            return true;
	        }
	        return false;
	    }

	    @SuppressWarnings("unchecked")
	    private static <T> T unproxyObject(T object) {
	    	
	       	//hide password
	    	if (object instanceof User) {
				User u = (User) object;
				u.setPassword(null);
			}
	        if (isProxy(object)) {
	            if (object instanceof PersistentCollection) {
	                PersistentCollection persistentCollection = (PersistentCollection) object;
	                return (T) unproxyPersistentCollection(persistentCollection);
	            } else if (object instanceof HibernateProxy) {
	                HibernateProxy hibernateProxy = (HibernateProxy) object;
	                return (T) unproxyHibernateProxy(hibernateProxy);
	            } else {
	                return null;
	            }
	        }
	        return object;
	    }

	    private static Object unproxyHibernateProxy(HibernateProxy hibernateProxy) {
	        Object result = hibernateProxy.writeReplace();
	        if (!(result instanceof SerializableProxy)) {
	            return result;
	        }
	        return null;
	    }


	    private static Object unproxyPersistentCollection(PersistentCollection persistentCollection) {
	        if (persistentCollection instanceof PersistentSet) {
	            return unproxyPersistentSet((Map<?, ?>) persistentCollection.getStoredSnapshot());
	        }
	        return persistentCollection.getStoredSnapshot();
	    }


	    private static <T> Set<T> unproxyPersistentSet(Map<T, ?> persistenceSet) {
	    	if(persistenceSet==null){
	    		return null;
	    	}
	        return new LinkedHashSet<T>(persistenceSet.keySet());
	    }


		
}
