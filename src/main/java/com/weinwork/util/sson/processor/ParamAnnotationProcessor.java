package com.weinwork.util.sson.processor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;
import com.weinwork.util.sson.attribute.FieldSerialize;
import com.weinwork.util.sson.attribute.ModelSerialize;
import com.weinwork.util.sson.map.FieldSerializeMap;
import com.weinwork.util.sson.map.ModelSerializeMap;
import com.weinwork.util.sson.util.ParamNameBindUtil;

public abstract class ParamAnnotationProcessor extends ServletModelAttributeMethodProcessor
{
	@Autowired
	private RequestMappingHandlerAdapter requestMappingHandlerAdapter;
	private final Map<Class<?>, Map<String, FieldSerializeMap>> replaceMap = new ConcurrentHashMap<Class<?>, Map<String, FieldSerializeMap>>();
	
	public ParamAnnotationProcessor()
	{
		super(false);
	}
	
	public ParamAnnotationProcessor(boolean annotationNotRequired)
	{
		super(annotationNotRequired);
	}
	
	@Override
	public boolean supportsParameter(MethodParameter parameter)
	{
		return !BeanUtils.isSimpleProperty(parameter.getParameterType()) && Arrays.stream(parameter.getParameterType().getDeclaredFields()).anyMatch(new Predicate<Field>() {
			@Override
			public boolean test(Field field)
			{
				return field.getAnnotation(FieldSerialize.class) != null;
			}
		});
	}
	
	@Override
	protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest nativeWebRequest)
	{
		Object target = binder.getTarget();
		Map<String, FieldSerializeMap> paramMappings = this.getParamMappings(target.getClass());
		ParamNameBindUtil paramNameDataBinder = new ParamNameBindUtil(target, binder.getObjectName(), paramMappings);
		
		requestMappingHandlerAdapter.getWebBindingInitializer().initBinder(paramNameDataBinder, nativeWebRequest);
		super.bindRequestParameters(paramNameDataBinder, nativeWebRequest);
	}
	
	private Map<String, FieldSerializeMap> getParamMappings(Class<?> targetClass)
	{
		Map<String, FieldSerializeMap> paramMappings = new HashMap<String, FieldSerializeMap>();
		ModelSerialize modelSerialize = targetClass.getDeclaredAnnotation(ModelSerialize.class);
		Field[] fields = targetClass.getDeclaredFields();
		
		if (modelSerialize == null || replaceMap.containsKey(targetClass))
		{
			return replaceMap.get(targetClass);
		}
		
		for (Field field : fields)
		{
			FieldSerialize fieldSerialize = field.getAnnotation(FieldSerialize.class);
			
			if (fieldSerialize != null && !fieldSerialize.paramName().isEmpty() && !fieldSerialize.paramIgnore())
			{
				paramMappings.put(fieldSerialize.paramName(), new FieldSerializeMap(fieldSerialize));
			}
		}
		
		replaceMap.put(targetClass, paramMappings);
		
		return paramMappings;
	}
	
}
