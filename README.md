# Baldur's Gate 3 Module for Candor

This module enables Baldur's Gate 3 support in Candor Mod Manager.

# This is very much WIP and does not include the ability to uninstall currently!
download candor and the mod manager from the releases tab

# How should a mod be laid out?

You can either follow the the steps below,  
Or use the Tool from ShinyHobo, which can be found [here](https://github.com/ShinyHobo/bg3-mod-packer)

# Mods should be a zip archive

* Mods should include all .pak files in the root of the archive
* Along with an "info.json" file

# What will be archive look like?
.  
+-- someName.pak  
+-- someOtherName.pak (multiple pak's are optional)  
+-- info.json

# What is an info.json file?

The info.json contains infomation about your mod that is required by the game to load your mod.  
it may look similar to this:
folderName must match that of the .pak file!

```json
{
  "mods": [
    {
      "modName": "someName",
      "UUID": "6b0cd32d-4bf6-462a-8082-2c76d9fbbd22",
      "folderName": "someName",
      "version": "789749832",
      "MD5": ""
    },
    {
      "modName": "someOtherName",
      "UUID": "6b0cd32d-4bf6-462a-8082-2c76d9fbbd22",
      "folderName": "someOtherName",
      "version": "789749832",
      "MD5": ""
    }
    ]
}
```
