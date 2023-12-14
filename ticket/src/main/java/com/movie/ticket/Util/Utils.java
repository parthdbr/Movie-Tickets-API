package com.movie.ticket.Util;

import com.mongodb.lang.Nullable;
import com.movie.ticket.Annotation.Access;
import com.movie.ticket.model.RestAPIs;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class Utils {

    public static List<RestAPIs> getAllMethodNames(Class className) {
        Method[] methods = className.getMethods();
        List<RestAPIs> apis = new ArrayList<>();
        for (Method method : methods) {
            if (Modifier.isPublic(method.getModifiers())) {
                RestAPIs restAPI = getRetApiByMethod(method);
                if (restAPI != null)
                    apis.add(restAPI);
            }
        }
        return apis;
    }

    @Nullable
    public static RestAPIs getRetApiByMethod(Method method) {
        Access access = method.getAnnotation(Access.class);

        String apiName = getApiName(method);

        if (apiName != null && access != null) {
            RestAPIs restAPIs = new RestAPIs();
            List<String> roles = new ArrayList<>(Arrays.asList(access.roles()))
                    .stream().map(Enum::toString).toList();
            restAPIs.setName(apiName);
            restAPIs.setRoles(roles);
            return restAPIs;
        }
        return null;
    }

    private static String getApiName(Method method) {
        String apiName = null;

        if (method.isAnnotationPresent(GetMapping.class))
            apiName = method.getAnnotation(GetMapping.class).value()[0];
        else if (method.isAnnotationPresent(PostMapping.class))
            apiName = method.getAnnotation(PostMapping.class).value()[0];
        else if (method.isAnnotationPresent(PutMapping.class))
            apiName = method.getAnnotation(PutMapping.class).value()[0];
        else if (method.isAnnotationPresent(DeleteMapping.class))
            apiName = method.getAnnotation(DeleteMapping.class).value()[0];
        else if (method.isAnnotationPresent(PatchMapping.class))
            apiName = method.getAnnotation(PatchMapping.class).value()[0];
        else if (method.isAnnotationPresent(RequestMapping.class))
            apiName = method.getAnnotation(RequestMapping.class).value()[0];

        return apiName;


    }

}
