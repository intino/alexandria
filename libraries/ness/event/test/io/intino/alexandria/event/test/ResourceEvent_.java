//package io.intino.alexandria.event.test;
//
//
//import io.intino.alexandria.event.resource.ResourceEvent;
//
//public class ResourceEvent_ {
//
//	public static void main(String[] args) {
//
//		datalake.resourceStore();
//		String s = anomaly.logResource();
//
//		ResourceEvent event;
//
//		Optional<Resource> resource = terminal.datalake().resourceStore().find(anomaly.logResource());
//
//		// Attribute log as Resource
//
////		ResourceEvent event = new ResourceEvent("the_type", "the_ss");
////
////		//event.attach("my-resource-1", file | url | path);
////		//event.attach("my-resource-2", file | url | path);
////		event.addResource("my-resource-1", file | url | path);
////		event.addResource("my-resource-2", file | url | path);
////		// ...
////
////		//Map<String, Attachment> attachments = event.attachments();
////		Map<String, Resource> resources = event.resources();
////
////		Resource resource = event.resource("my-resource-1");
////
////		if(resource == null) return; // resource was not declared in this event, skip
////		if(!resource.isAvailable()) return; // checks whether the resource is available for reading or not (found in the DL)
////
////		//System.out.println(resource.uri());
////		//File file = new File(resource.uri());
////
////		try(InputStream inputStream = resource.open()) {
////			// Read resource data. Throws exception if not available
////		} catch(IOException e) {
////			//...
////		}
//	}
//}
