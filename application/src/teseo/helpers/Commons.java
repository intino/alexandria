package teseo.helpers;

import teseo.Application;
import teseo.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Commons {

    public static void writeFrame(File packageFolder, String name, String format) {
        try {
            packageFolder.mkdirs();
            File file = javaFile(packageFolder, name);
            Files.write(file.toPath(), format.getBytes());
//            Logger.getGlobal().info(file.getAbsolutePath() + " generated");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File javaFile(File packageFolder, String name) {
        return new File(packageFolder, prepareName(name) + ".java");
    }

    private static String prepareName(String name) {
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    public static String[] pathParameters(Resource resource) {
        return resource.parameterList().stream().filter(p -> p.in() == Resource.Parameter.In.path)
                .map(Resource.Parameter::name).toArray(String[]::new);
    }

    public static String format(String path) {
        return path.isEmpty() ? "" : path + "/";
    }

    public static String path(Resource resource) {
        String basePath = cleanPath(resource.ownerAs(Application.class).path());
        String path = cleanPath(resource.ownerAs(Application.Api.class).path());
        String resourcePath = cleanPath(resource.path());
        return format(basePath) + format(path) + resourcePath;
    }

    private static String cleanPath(String path) {
        path = path.endsWith("/") ? path.substring(path.length() - 1) : path;
        return path.startsWith("/") ? path.substring(1) : path;
    }

    public static String returnType(List<Resource.Response> responses) {
        Resource.Response response = successResponse(responses);
        if (response == null || response.asType() == null) return "void";
        return response.asType().type();
    }

    public static Resource.Response successResponse(List<Resource.Response> responses) {
        return responses.stream().filter(r -> r.code().toString().contains("Success"))
                .findFirst().orElse(null);
    }

    public static List<Resource.Response> nonSuccessResponse(List<Resource.Response> responses) {
        return responses.stream().filter(r -> !r.code().toString().contains("Success")).collect(toList());
    }
}
