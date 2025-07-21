package academy.teenfuture.crse.service;

import academy.teenfuture.crse.modal.DemoUser;
import academy.teenfuture.crse.repository.DemoUserRepository;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;
import java.util.Optional;

@Service
public class DemoAuthService {
    private final DemoUserRepository repository;

    public DemoAuthService(DemoUserRepository repository) {
        this.repository = repository;
    }

    public DemoUser login(String email, String password) throws AuthenticationException {
        DemoUser user = repository.findByEmail(email).orElseThrow(AuthenticationException::new);
        if (!user.getPassword().equals(password)) {
            throw new AuthenticationException("Incorrect password");
        }
        return user;
    }

    public DemoUser createUser(String email, String password, String phoneNumber) throws Exception {
      Optional<DemoUser> existingUser = repository.findByEmail(email);
      if (existingUser.isPresent()) {
          throw new Exception("Email already exist");
      }
  
      DemoUser user = new DemoUser();
      user.setEmail(email);
      user.setPassword(password);
      user.setPhoneNumber(phoneNumber);
      return repository.save(user);
  }
  
  
}