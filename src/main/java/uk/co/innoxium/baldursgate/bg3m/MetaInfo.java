package uk.co.innoxium.baldursgate.bg3m;

import com.google.gson.JsonObject;
import org.dom4j.Element;

public class MetaInfo {

    private String author;
    private String name;
    private String folder;
    private String version;
    private String description;
    private String uuid;

    private final MetaType type;

    public MetaInfo(MetaType type) {

        this.type = type;
    }

    public String getAuthor() {

        return author;
    }

    public String getName() {

        return name;
    }

    public String getFolder() {

        return folder;
    }

    public String getVersion() {

        return version;
    }

    public String getDescription() {

        return description;
    }

    public String getUuid() {

        return uuid;
    }

    public enum MetaType {

        V1, // v1 is for the original info.json format.
        V2, // v2 is for the second format, enabling multipak support
        V3 // v3 is the latest, which is more descriptive
    }

    public MetaInfo fromJson(JsonObject obj) {

        MetaInfo ret;

        switch(type) {

            case V1 -> {

                ret = buildV1(obj);
            }
            case V2 -> {

                ret = buildV2(obj);
            }
            case V3 -> {

                ret = buildV3(obj);
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
        return ret;
    }

    private MetaInfo buildV1(JsonObject obj) {

        this.name = obj.get("modName").getAsString();
        this.author = "";
        this.folder = obj.get("modFolder").getAsString();
        this.version = obj.get("version").getAsString();
        this.description = "";
        this.uuid = obj.get("uuid").getAsString();
        return this;
    }

    private MetaInfo buildV2(JsonObject obj) {

        this.name = obj.get("modName").getAsString();
        this.author = "";
        this.folder = obj.get("modFolder").getAsString();
        this.version = obj.get("version").getAsString();
        this.description = "";
        this.uuid = obj.get("uuid").getAsString();
        return this;
    }

    private MetaInfo buildV3(JsonObject obj) {

        this.name = obj.get("Name").getAsString();
        this.author = obj.get("Author").getAsString();
        this.description = obj.get("Description").getAsString();
        this.version = obj.get("Version").getAsString();
        this.folder = obj.get("Folder").getAsString();
        this.uuid = obj.get("UUID").getAsString();
        return this;
    }

    public Element toModOrder(Element parent) {

        Element moduleNode = parent.addElement("node").addAttribute("id", "Module");

        return moduleNode
                .addElement("attribute")
                .addAttribute("id", "UUID")
                .addAttribute("type", "FixedString")
                .addAttribute("value", this.uuid);
    }

    public Element toModuleShortDesc(Element parent) {

        Element moduleShortDesc = parent.addElement("node").addAttribute("id", "ModuleShortDesc");
        addMSDAttribute(moduleShortDesc, "Folder", "LSWString", this.folder);
        addMSDAttribute(moduleShortDesc, "MD5", "LSString", "");
        addMSDAttribute(moduleShortDesc, "Name", "FixedString", this.name);
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

    public void removeModShortDesc(Element modChildren) {

        modChildren.selectNodes("node").forEach(node -> {

            Element element = (Element)node;
            element.attributes().forEach(attr -> {

                if(attr.getValue().equalsIgnoreCase("ModuleShortDesc")) {

                    element.selectNodes("attribute").forEach(msdNode -> {

                        Element msdElement = (Element)msdNode;
                        msdElement.attributes().forEach(msdAttr -> {

                            if(msdAttr.getValue().equalsIgnoreCase("Folder")) {

                                if(msdElement.attribute("value").getValue().equalsIgnoreCase(this.folder)) {

                                    modChildren.remove(node);
                                }
                            }
                        });
                    });
                }
            });
        });
    }

    public void removeModOrder(Element modOrderChildren) {

        modOrderChildren.selectNodes("node").forEach(moduleNode -> {

            Element moduleElement = (Element)moduleNode;

            if(moduleElement.attribute("id").getValue().equalsIgnoreCase("Module")) {

                moduleElement.selectNodes("attribute").forEach(attrNode -> {

                    Element attrElement = (Element)attrNode;
                    attrElement.attributes().forEach(attr -> {

                        if(attr.getValue().equalsIgnoreCase("UUID")) {

                            if(attrElement.attribute("value").getValue().equalsIgnoreCase(this.uuid)) {

                                modOrderChildren.remove(moduleNode);
                            }
                        }
                    });
                });
            }
        });
    }
}
