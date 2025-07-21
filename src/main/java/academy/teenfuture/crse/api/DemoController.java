package academy.teenfuture.crse.api;

import academy.teenfuture.crse.api.Demo;
import academy.teenfuture.crse.requestModal.DemoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;

@RequestMapping("demo")
@RestController
public class DemoController {

  //@Autowired
  //DemoService demoService;

  public DemoController() {
  }

  /**
   * Get all existing Demo(s)
   * 
   * @return Get all Demo object(s)
   */
  @GetMapping
  public ResponseEntity<List<Demo>> getAllDemos() {
    System.out.println("GET all demo ");

    // Simulate returning all Demo object(s)
    Demo demo1 = new Demo(1);
    Demo demo2 = new Demo(3);
    demo2.setAbc("My new CBA");
    ArrayList<Demo> demos = new ArrayList<Demo>();
    demos.add(demo1);
    demos.add(demo2);

    return ResponseEntity.ok(demos);
  }

  /**
   * Get a Demo by input demo ID
   * 
   * @param demoId (OPTIONAL) eg. /512, assume only ONE id for each HTTP POST
   * @return new created Demo object in json
   */
  @GetMapping("/{demoId}")
  public ResponseEntity<List<Demo>> getDemosById(@PathVariable Long demoId) {
    System.out.println("GET demo/{demoId}");

    // Simulate returning selected Demo object
    Demo demo1 = new Demo(demoId);
    ArrayList<Demo> demos = new ArrayList<Demo>();
    demos.add(demo1);
    return ResponseEntity.ok(demos);
  }

  /**
   * Create Demo by input json object
   * 
   * @param jsonData (OPTIONAL) post json format data, eg. {"id": "412", "active":
   *                 true, "name": "the name"}
   * @return new created Demo object in json
   */
  @PostMapping
  public ResponseEntity<List<Demo>> createDemo(@RequestBody DemoRequest request) {
    System.out.println("POST demo with json object");

    Demo demo1;

    // handle by jsonData
    demo1 = new Demo(request.getId());
    demo1.setName(request.getName());
    demo1.setActive(request.isActive());

    ArrayList<Demo> demos = new ArrayList<Demo>();
    demos.add(demo1);
    return ResponseEntity.ok(demos);

  }

  /**
   * Create Demo by input json object
   * 
   * @param jsonData (OPTIONAL) post json format data, eg. {"id": "412", "active":
   *                 true, "name": "the name"}
   * @return new created Demo object in json
   */
  @PostMapping("/")
  public ResponseEntity<List<Demo>> createDemoRoot(@RequestBody DemoRequest request) {
    System.out.println("POST demo/ with json object");

    Demo demo1;

    // handle by jsonData
    demo1 = new Demo(request.getId());
    demo1.setName(request.getName());
    demo1.setActive(request.isActive());

    ArrayList<Demo> demos = new ArrayList<Demo>();
    demos.add(demo1);
    return ResponseEntity.ok(demos);

  }

  /**
   * Update Demo by input corresponding Demo ID with new content by json object
   * 
   * @param demoId   (OPTIONAL) eg. /512, assume only ONE id for each HTTP POST
   * @param jsonData (OPTIONAL) post json format data, eg. {"id": "512", "active":
   *                 true, "name": "the name"}
   * @return Updated Demo object in json
   */
  @PutMapping("/{demoId}")
  public ResponseEntity<List<Demo>> updateDemoById(@PathVariable Long demoId, @RequestBody DemoRequest request) {
    System.out.println("PUT demo/{demoId} with json object");

    Demo demo1;

    // handle by jsonData
    demo1 = new Demo(request.getId());
    demo1.setName(request.getName());
    demo1.setActive(request.isActive());

    ArrayList<Demo> demos = new ArrayList<Demo>();
    demos.add(demo1);
    return ResponseEntity.ok(demos);

  }

  /**
   * Remove one Demo by input corresponding Demo ID
   * 
   * @param demoId (OPTIONAL) eg. /512, assume only ONE id for each HTTP POST
   * @return Removed Demo object in json or 404 NOT found
   */
  @DeleteMapping("/{demoId}")
  public ResponseEntity<List<Demo>> rmDemoById(@PathVariable Long demoId) {
    System.out.println("DEL demo/{demoId}");

    Demo demo1;

    // handle by Demo ID
    demo1 = new Demo(demoId);

    ArrayList<Demo> demos = new ArrayList<Demo>();
    demos.add(demo1);

    // verify the result of removing this demo item
    // var isRemoved = postService.delete(id);
    boolean isRemoved;
    if (demoId != 112)
      isRemoved = true;
    else
      isRemoved = false;

    if (!isRemoved) {

      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.ok(demos);

  }

}