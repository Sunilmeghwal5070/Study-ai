import okhttp3.HttpUrl;
public class TestRetrofit {
    public static void main(String[] args) {
        HttpUrl baseUrl = HttpUrl.parse("https://generativelanguage.googleapis.com/");
        HttpUrl url = baseUrl.resolve("v1beta/models/gemini-1.5-flash:generateContent");
        System.out.println(url.toString());
    }
}
