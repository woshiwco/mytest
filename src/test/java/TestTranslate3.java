import com.google.cloud.translate.Detection;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import org.junit.Test;

public class TestTranslate3 {
    @Test
    public void test() {
        Translate translate = TranslateOptions.getDefaultInstance().getService();
        Detection language = translate.detect("hello");
        Translation translation = translate.translate("hello world.",
                Translate.TranslateOption.sourceLanguage("en"),
                Translate.TranslateOption.targetLanguage("zh"),
                Translate.TranslateOption.model("base"));
        String translatedText = translation.getTranslatedText();
    }
}
