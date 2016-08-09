package com.ap.misc.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import org.axonframework.common.Assert;

import com.ap.config.base.Constants;
import com.ap.domain.base.SessionDto;

public class ValidatorUtil {
	
	public static boolean isPresent(String str){
		return str != null && !str.equals("");
	}

	public static boolean isPresent(Integer val){
		return val != null && val != 0;
	}
	
	public static boolean isPresent(BigDecimal val){
		return val != null && val != BigDecimal.ZERO;
	}
	
	public static <T> boolean isPresent(List<T> list){
		return list != null && !list.isEmpty();
	}
	
	public static boolean isPresent(ArrayList<Object> array){
		return array != null && !array.isEmpty();
	}
	
	public static boolean isPresent(Object obj){
		return obj != null;
	}
	
	public static boolean isPresent(Object[] objs){
		return objs != null && objs.length > 0;
	}
	
	public static boolean isValidClass(Object obj, Class clasz){
		return ( clasz.isInstance(obj));
	}
	
	//Session validator
	public static boolean isGuest(SessionDto session){
		Assert.notNull(session, "session is null");
		Assert.notNull(session.getUserName(), "session username is null");
		return session.getUserName().equalsIgnoreCase(Constants.DEFAULT_USER);
	}
	
	public static void protectGuest(SessionDto session){
		if (isGuest(session)) {
			throw new IllegalStateException("Guest account access forbiden");
		}
	}
}
