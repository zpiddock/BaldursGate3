import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;
import uk.co.innoxium.baldursgate.BG3Settings;
import uk.co.innoxium.baldursgate.BaldursGateModule;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ModSettingTest {

    public static void main(String... args) {

        File modsetings = new File(BaldursGateModule.playerProfiles, "TestingProfile" + "/modsettings.lsx");
        Document doc = null;
        try {

            doc = getSaxReader().read(modsetings);
        } catch (DocumentException e) {

            e.printStackTrace();
        }

        AtomicReference<Element> module = new AtomicReference<>();
        List<Node> nodes = doc.selectNodes("//save/region/node/children/node");
        nodes.forEach(node -> {

            Element element = (Element)node;
            element.attributeIterator().forEachRemaining(attr -> {

                if(attr.getName().equalsIgnoreCase("id") && attr.getValue().equalsIgnoreCase("mods")) {

                    module.set(element);
                }
            });
        });
    }

    private static SAXReader getSaxReader() {

        SAXReader reader = SAXReader.createDefault();
        try {

            reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", true);
            reader.setFeature("http://xml.org/sax/features/external-general-entities", true);
            reader.setFeature("http://xml.org/sax/features/external-parameter-entities", true);
        } catch (SAXException e) {

            e.printStackTrace();
        }
        reader.setEncoding(StandardCharsets.UTF_8.name());
        return reader;
    }
}
