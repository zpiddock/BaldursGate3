package uk.co.innoxium.baldursgate.bg3m;

import com.google.gson.JsonObject;
import org.dom4j.Element;

/**
 * Allows for json binding.
 */
public class BG3Mod {

    private BG3Mod() {}

    public String modName;
    public String uuid;
    public String folderName;
    public String version;
    public String MD5;

    public static BG3Mod fromJson(JsonObject obj) {

        BG3Mod mod = new BG3Mod();
        mod.modName = obj.get("modName").getAsString();
        mod.uuid = obj.get("GUID").getAsString();
        mod.folderName = obj.get("folderName").getAsString();
        mod.version = obj.get("version").getAsString();
        mod.MD5 = obj.get("MD5").getAsString();

        return mod;
    }

    public Element toModOrder(Element parent) {

        Element moduleNode = (Element) parent.selectSingleNode("node");
        if(moduleNode == null) {

            moduleNode = parent.addElement("node").addAttribute("id", "Module");
        }

        return moduleNode
                .addElement("attribute")
                .addAttribute("id", "UUID")
                .addAttribute("type", "FixedString")
                .addAttribute("value", this.uuid);
    }

    public Element toModuleShortDesc(Element parent) {

        Element moduleShortDesc = parent.addElement("node").addAttribute("id", "ModuleShortDesc");
        addMSDAttribute(moduleShortDesc, "Folder", "LSWString", this.folderName);
        addMSDAttribute(moduleShortDesc, "MD5", "LSString", this.MD5);
        addMSDAttribute(moduleShortDesc, "Name", "FixedString", this.modName);
        addMSDAttribute(moduleShortDesc, "UUID", "FixedString", this.uuid);
        addMSDAttribute(moduleShortDesc, "Version", "int32", this.version);
        return moduleShortDesc;
    }

    // Adds an element to the <node id=ModuleShortDesc> node
    private Element addMSDAttribute(Element moduleShortDesc, String id, String type, String value) {

        return moduleShortDesc.addElement("attribute")
                .addAttribute("id", id)
                .addAttribute("type", type)
                .addAttribute("value", value);
    }
}
