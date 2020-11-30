package com.demo.swagger;

import com.jfinal.core.Controller;

import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.models.Contact;
import io.swagger.models.Info;
import io.swagger.models.License;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.Scheme;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


/**
 * com.demo.swagger
 * create by admin nihui
 * create time 2020/11/30
 * version 1.0
 **/
public class SwaggerController extends Controller {
    //设置需要扫描哪个包里面的类生产api
    public static final String BASE_PACKAGE = "com.demo.index";

    public void index() {
        render("index.html");
    }

    public void api() {
        Swagger doc = new Swagger();
        Info info = new Info();
        Contact contact = new Contact();
        contact.setEmail("admin@bimfoo.com");
        contact.setName("bimfoo");
        contact.setUrl("http://www.baidu.com");

        info.setDescription("项目接口的总体描述信息");
        License license = new License();
        license.setName("Apache 2.0");
        license.setUrl("http://www.baidu.com");
        info.setLicense(license);
        info.setTitle("运营平台接口");
        info.setTermsOfService("http://www.baidu.com");
        info.setVersion("2.0");
        info.setContact(contact);

        List<Scheme> schemes = new ArrayList<>();
        schemes.add(Scheme.HTTP);
        schemes.add(Scheme.HTTPS);

        Map<String, Path> paths = new HashMap<>();

        Set<Class<?>> classSet = getClassSet(BASE_PACKAGE);
        List<Tag> tags = new ArrayList<>();
        for (Class<?> cls : classSet) {
            if (cls.isAnnotationPresent(Api.class)) {
                Api api = cls.getAnnotation(Api.class);
                String[] apitags = api.tags();
                String apiValue = api.value();

                if (apitags.length > 0) {
                    for (String tag : apitags) {
                        Tag tagobj = new Tag();
                        tagobj.setName(tag);
                        tags.add(tagobj);
                    }
                } else {
                    Tag tagobj = new Tag();
                    tagobj.setName(apiValue);
                    tags.add(tagobj);
                }

                Method[] methods = cls.getMethods();

                for (Method method : methods) {
                    Annotation[] annotations = method.getAnnotations();
                    Path path = new Path();

                    for (Annotation annotation : annotations) {
                        // Class<?> aclass = annotation.annotationType();
                        Operation option = new Operation();
                        String opvalue = "";
                        if (method.isAnnotationPresent(ApiOperation.class)) {
                            ApiOperation opertion = method.getAnnotation(ApiOperation.class);
                            ArrayList<String> produces = new ArrayList<>();
                            String producesStr = opertion.produces();
                            produces.add(producesStr);

                            opvalue = opertion.value();
                            String notes = opertion.notes();
                            /*
                             * String consumesStr = opertion.consumes();
                             * String[] ptagsarray = opertion.tags();
                             */
                            List<String> ptags = new ArrayList<>();
                            ptags.add(apitags[0]);
                            /*
                             * if(ptagsarray.length>0){ for(String
                             * tag:ptagsarray){ ptags.add(tag); } }
                             */

                            option.setConsumes(new ArrayList<String>());
                            option.setDescription(notes);
                            option.setSummary(notes);
                            option.setTags(ptags);

                        }
                        List<Parameter> parameters = new ArrayList<>();
                        if (method.isAnnotationPresent(ApiImplicitParams.class)) {
                            ApiImplicitParams apiImplicitParams = method.getAnnotation(ApiImplicitParams.class);
                            ApiImplicitParam[] apiImplicitParamArray = apiImplicitParams.value();

                            for (ApiImplicitParam param : apiImplicitParamArray) {
                                PathParameter parameter = new PathParameter();
                                String in = param.paramType();
                                String name = param.name();
                                String value = param.value();
                                boolean required = param.required();
                                String dataType = param.dataType();

                                parameter.setType(dataType);
                                parameter.setDefaultValue("");
                                parameter.setDescription(value);
                                parameter.setRequired(required);
                                parameter.setIn(in);
                                parameter.setName(name);
                                parameters.add(parameter);
                            }
                        }
                        option.setParameters(parameters);

                        Map<String, Response> responseMap = new HashMap<>();
                        if (method.isAnnotationPresent(ApiResponses.class)) {
                            ApiResponses responses = method.getAnnotation(ApiResponses.class);
                            ApiResponse[] responseArray = responses.value();
                            for (ApiResponse response : responseArray) {
                                String code = response.code() + "";
                                String msg = response.message();
                                Response res = new Response();
                                res.setDescription(msg);
                                responseMap.put(code, res);

                            }

                        }

                        option.setResponses(responseMap);
                        path.setGet(option);
                        paths.put(opvalue, path);
                    }

                }
                doc.setSchemes(schemes);
                doc.setSwagger("2.0");
//				doc.setBasePath("/user");
                doc.setInfo(info);
                doc.setHost("localhost:8080");
                doc.setTags(tags);
                doc.setPaths(paths);

            }

        }
        Gson gson = new Gson();
        String json = gson.toJson(doc);
        renderText(json);
    }

    /**
     * 获取指定包名下所有类
     *
     * @param packageName
     * @return
     */
    public static Set<Class<?>> getClassSet(String packageName) {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        try {
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();
                    if (protocol.equals("file")) {
                        String packagePath = url.getPath().replace("%20", " ");
                        addClass(classSet, packagePath, packageName);
                    } else if (protocol.equals("jar")) {
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        if (jarURLConnection != null) {
                            JarFile jarFile = jarURLConnection.getJarFile();
                            if (jarFile != null) {
                                Enumeration<JarEntry> jarEntries = jarFile.entries();
                                while (jarEntries.hasMoreElements()) {
                                    JarEntry jarEntry = jarEntries.nextElement();
                                    String jarEntryName = jarEntry.getName();
                                    if (jarEntryName.endsWith(".class")) {
                                        String className = jarEntryName.substring(0, jarEntryName.lastIndexOf("."))
                                                .replaceAll("/", ".");
                                        doAddClass(classSet, className);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return classSet;
    }

    /**
     * 获取类加载器
     *
     * @return
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            public boolean accept(File file) {
                return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
            }
        });
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (StringUtils.isNotBlank(packageName)) {
                    className = packageName + "." + className;
                }
                doAddClass(classSet, className);
            } else {
                String subPackagePath = fileName;
                if (StringUtils.isNotBlank(packagePath)) {
                    subPackagePath = packagePath + "/" + subPackagePath;
                }
                String subPackageName = fileName;
                if (StringUtils.isNotBlank(packageName)) {
                    subPackageName = packageName + "." + subPackageName;
                }
                addClass(classSet, subPackagePath, subPackageName);
            }
        }
    }

    private static void doAddClass(Set<Class<?>> classSet, String className) {
        Class<?> cls = loadClass(className, false);
        classSet.add(cls);
    }

    /**
     * 加载类
     *
     * @param className
     * @param isInitialized false 代表装载类的时候 不进行初始化工作[不会执行静态代码块]
     * @return
     */
    public static Class<?> loadClass(String className, boolean isInitialized) {
        Class<?> cls;
        try {
            cls = Class.forName(className, isInitialized, getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return cls;
    }

}
