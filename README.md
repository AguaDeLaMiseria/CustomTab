# CustomTab
 Light-weight solution for custom tab completions!
 
It supports both whitelist and blacklist mode, and has the option to block the execution of commands not included depending on list type.

You can also set up custom tab groups, so you can add more tab completions to certain players through permissions. The permissions are in the form of "customtab.group.[groupname]".

To bypass (get access to all completions) you can grant the "customtab.bypass" permission, and to reload the plugin: "customtab.reload".

If after configuring the plugin in blacklist mode, completions are still appearing in the form of "/plugin:command", you can disable this by setting "send-namespaced" to false in spigot.yml under "commands" section. Because of how the spigot API works, you can only set existing commands to tab completions.
