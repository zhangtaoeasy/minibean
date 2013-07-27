package org.johngao.minibean.util;

import java.lang.reflect.Field;

import org.johngao.minbean.core.AnnotationResolver;
import org.johngao.minibean.impl.core.ClassAnnotationResolver;
import org.johngao.minibean.impl.core.FieldAnnotationResolver;

public abstract class AnnotationContext {
	/**
	 * 调用自动赋值，支持任意修饰字段
	 * 
	 * @author JohnGao
	 * 
	 * @param goalObject
	 *            目标对象, 即需要被自动赋值的对象
	 * 
	 * @param sourceObject
	 *            源对象
	 * 
	 * @return void
	 */
	public static <T, K> void setParams(T goalObject, K sourceObject) {
		/* 检测目标对象是否标记有@Assignment注解 */
		if (new ClassAnnotationResolver().classResolver(goalObject)) {
			/* 字段注解解析后, 实现自动赋值 */
			new FieldAnnotationResolver().fieldResolver(goalObject,
					sourceObject);
		}
	}
}