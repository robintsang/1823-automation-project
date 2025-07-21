package academy.teenfuture.crse.api;

import academy.teenfuture.crse.service.MemberPointCollectionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;

import academy.teenfuture.crse.modal.Member;

@Controller
@RequestMapping("/member")
public class MemberPointCollectionController {
  final MemberPointCollectionService service;

  public MemberPointCollectionController(MemberPointCollectionService service) {
    this.service = service;

  }

@PostMapping("/{id}") 
public ResponseEntity<Member> getMember(@PathVariable("id") int id){
  Member member = this.service.getMember(id);
  if(null==member)
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  return ResponseEntity.ok(member);
}

  @PostMapping("/add/{id}/{point}") 
  public ResponseEntity<Member> addPoint(@PathVariable("id") int id, @PathVariable("point") int point) {
      Member member = this.service.addPoint(id , point);
      return ResponseEntity.ok(member);
  }
  
  @PostMapping("/create") 
  public ResponseEntity<Member> createMember(){
    Member member = this.service.create(new Member());
    return ResponseEntity.ok(member);
  }

}
