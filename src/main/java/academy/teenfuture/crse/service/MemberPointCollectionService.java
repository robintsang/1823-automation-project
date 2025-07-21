package academy.teenfuture.crse.service; 

import academy.teenfuture.crse.modal.Member;
import academy.teenfuture.crse.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberPointCollectionService {

  public MemberRepository repository;

  public MemberPointCollectionService(MemberRepository repository){
    this.repository = repository;
  }
  public Member getMember(int id){
    return this.repository.findById(id);
  }
  public Member addPoint(int id, int point){
    Member member = this.repository.findById(id);
    member.setPoint(member.getPoint()+point);
    this.repository.save(member);
    return member;
  }
  public Member create(Member member){
    return this.repository.save(member);
  }
}