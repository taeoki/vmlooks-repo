package controller;

import static org.ovirt.engine.sdk4.ConnectionBuilder.connection;
import static org.ovirt.engine.sdk4.builders.Builders.cluster;
import static org.ovirt.engine.sdk4.builders.Builders.host;
import static org.ovirt.engine.sdk4.builders.Builders.network;
import static org.ovirt.engine.sdk4.builders.Builders.vlan;
import static org.ovirt.engine.sdk4.builders.Builders.dataCenter;

import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.services.NetworksService;
import org.ovirt.engine.sdk4.services.ClusterService;
import org.ovirt.engine.sdk4.services.HostsService;
import org.ovirt.engine.sdk4.services.VmsService;
import org.ovirt.engine.sdk4.types.Host;
import org.ovirt.engine.sdk4.types.Vm;
import org.ovirt.engine.sdk4.services.NetworksService;
import org.ovirt.engine.sdk4.types.NetworkUsage;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class OlvmController {
	
	static Connection connection;
	
	public OlvmController()
	{
		connect();
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
		File file = new File("");
		String path = file.getAbsolutePath();
		path = path+"/pki/ovlm.truststore";
		
              connection = connection()
            		  .url("https://localhost.localdomain/ovirt-engine/api")
                      .user("admin@internal")
                      .password("qweasd")
                      .trustStoreFile(path)
                      .trustStorePassword("mypass")
                      .build();
    }

	public void addNetwork()
	{
		NetworksService networksService = connection.systemService().networksService();
		
		networksService.add()
        .network(
            network()
            .name("mynetwork22222")
            .description("My logical network")
            .dataCenter(
                dataCenter()
                .name("apiDataCenter")
            )
            .vlan(
                vlan()
                .id(100)
            )
            .usages(Arrays.asList(NetworkUsage.DISPLAY))
            .mtu(1500)
        )
        .send();
		
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
