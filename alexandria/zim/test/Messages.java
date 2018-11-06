public class Messages {

    public static String MessageWithParentClass =
        "[Teacher]\n" +
        "name: Jose\n" +
        "money: 50.0\n" +
        "birthDate: 2016-10-04T20:10:12Z\n" +
        "university: ULPGC\n" +
        "\n" +
        "[Teacher.Country]\n" +
        "name: Spain\n";


    public static String EmptyAttributeMessage =
        "[Teacher]\n" +
        "name: Jose\n" +
        "money: 50.0\n" +
        "birthDate: 2016-10-04T20:10:11Z\n" +
        "university: ULPGC\n" +
        "\n" +
        "[Person.Country]\n" +
        "name: Spain\n" +
        "continent:\n";

    public static String OldFormatMessage =
        "[Teacher]\n" +
        "name = \"Jose\"\n" +
        "money=50.0\n" +
        "birthDate= 2016-10-04T20:10:12Z\n" +
        "university = ULPGC\n" +
        "\n" +
        "[Teacher.Country]\n" +
        "name=\"Spain\"\n" +
        "continent=\n";

    public static String MultipleComponentMessage =
        "[Teacher]\n" +
        "name: Jose\n" +
        "money: 50.0\n" +
        "birthDate: 2016-10-04T20:10:11Z\n" +
        "university: ULPGC\n" +
        "\n" +
        "[Teacher.Country]\n" +
        "name: Spain\n" +
        "\n" +
        "[Teacher.Phone]\n" +
        "value: +150512101402\n" +
        "\n" +
        "[Teacher.Phone.Country]\n" +
        "name: USA\n" +
        "\n" +
        "[Teacher.Phone]\n" +
        "value: +521005101402\n" +
        "\n" +
        "[Teacher.Phone.Country]\n" +
        "name: Mexico\n";

    public static String Stack =
            "java.lang.NullPointerException: Attempt to invoke interface method 'java.lang.Object java.util.List.get(int)' on a null object reference\n" +
                    "    at io.intino.consul.AppService$5.run(AppService.java:154)\n" +
                    "    at android.os.Handler.handleCallback(Handler.java:815)\n" +
                    "    at android.os.Handler.dispatchMessage(Handler.java:104)\n" +
                    "    at android.os.Looper.loop(Looper.java:194)\n" +
                    "    at android.app.ActivityThread.main(ActivityThread.java:5666)\n" +
                    "    at java.lang.reflect.Method.invoke(Native Method)\n" +
                    "    at java.lang.reflect.Method.invoke(Method.java:372)\n" +
                    "\n" +
                    "    at com.android.compiler.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:959)\n" +
                    "    at com.android.compiler.os.ZygoteInit.main(ZygoteInit.java:754)" +
					"\n" +
					"\n";


    public static String CrashMessage =
        "[Crash]\n" +
        "instant: 2017-03-21T07:39:00Z\n" +
        "app: io.intino.consul\n" +
        "deviceId: b367172b0c6fe726\n" +
        "stack:\n" + indent(Stack) + "\n";

    private static String Status1 =
            "[Status]\n" +
                    "battery: 78.0\n" +
                    "cpuUsage: 11.95\n" +
                    "isPlugged: true\n" +
                    "isScreenOn: false\n" +
                    "temperature: 29.0\n" +
                    "created: 2017-03-22T12:56:18Z\n";

    private static String Status2 =
            "[Status]\n" +
                    "battery: 78.0\n" +
                    "cpuUsage: 11.95\n" +
                    "isPlugged: true\n" +
                    "isScreenOn: true\n" +
                    "temperature: 29.0\n" +
                    "created: 2017-03-22T12:56:18Z\n";


    public static String StatusMessage =
        Status1 + "\n" + Status2;

    private static String indent(String text) {
        return "\t" + text.replaceAll("\\n", "\n\t");
    }

}
