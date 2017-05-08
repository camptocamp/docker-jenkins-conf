import jenkins.model.*;
import java.lang.reflect.Field;

def env = System.getenv()
hipchat_token = env['JENKINS_HIPCHAT_TOKEN']
hipchat_room = env['JENKINS_HIPCHAT_ROOM']
if (hipchat_token) {
  if ( Jenkins.instance.pluginManager.activePlugins.find { it.shortName == "hipchat" } != null ) {
    println "--> setting hipchat plugin"

    def descriptor = Jenkins.instance.getDescriptorByType(jenkins.plugins.hipchat.HipChatNotifier.DescriptorImpl.class)

    // no setters :-(
    // Groovy can disregard object's pivacy anyway to directly access private
    // fields, but we use a different technique 'reflection' this time
    Field[] fld = descriptor.class.getDeclaredFields();
    for(Field f:fld){
      f.setAccessible(true);
      switch (f.getName()) {
        case "v2Enabled"      : f.set(descriptor, true)
                              break
        case "server"         : f.set(descriptor, "api.hipchat.com")
                              break
        case "credentialId"   : f.set(descriptor, "hipchat-global-token")
                              break
        case "room"           : f.set(descriptor, hipchat_room)
                              break
        case "sendAs"         : f.set(descriptor, "Jenkins")
                              break
      }
    }
  }
}