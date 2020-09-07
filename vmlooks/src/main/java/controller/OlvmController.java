package controller;

import static org.ovirt.engine.sdk4.ConnectionBuilder.connection;
import static org.ovirt.engine.sdk4.builders.Builders.cluster;
import static org.ovirt.engine.sdk4.builders.Builders.host;

import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.services.HostsService;
import org.ovirt.engine.sdk4.services.VmsService;
import org.ovirt.engine.sdk4.types.Host;
import org.ovirt.engine.sdk4.types.Vm;

import java.util.List;

public class OlvmController {
	
	static Connection connection;
	
	public OlvmController()
	{
		connection();
	}
	
	public List<Vm> getAllVmList()
    {
              VmsService vmsService = connection.systemService().vmsService();
              List<Vm> vms = vmsService.list().send().vms();
              return vms;
    }
	
	public List<Host> getAllHostList()
    {
              HostsService hostsService = connection.systemService().hostsService();
              List<Host> hosts = hostsService.list().send().hosts();
             
              return hosts;
    }
	
	public void connect()
    {
              connection = connection()
            		  .url("https://localhost.localdomain/ovirt-engine/api")
                      .user("admin@internal")
                      .password("qweasd")
                      .trustStoreFile("/Users/tae.ok/Desktop/Pstore/.truststore")
                      .trustStorePassword("mypass")
                      .build();
    }

   
    public void disconnection()
    {
              try {
                         connection.close();
              } catch (Exception e) {
                         // TODO Auto-generated catch block
                         e.printStackTrace();
              }
    }
}
