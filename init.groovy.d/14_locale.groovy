if ( Jenkins.instance.pluginManager.activePlugins.find { it.shortName == "locale" } != null ) {
    def pluginWrapper = jenkins.model.Jenkins.instance.getPluginManager().getPlugin('locale')
    def plugin = pluginWrapper.getPlugin()
    plugin.setSystemLocale('en')
    plugin.ignoreAcceptLanguage = true
    Jenkins.instance.save()
}