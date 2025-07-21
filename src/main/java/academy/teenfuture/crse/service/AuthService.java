package academy.teenfuture.crse.service;

import academy.teenfuture.crse.Enum.UserRole;
import academy.teenfuture.crse.filter.Filter;
import academy.teenfuture.crse.modal.OAuthResponse;
import academy.teenfuture.crse.modal.User;
import academy.teenfuture.crse.repository.UserRepository;
import academy.teenfuture.crse.requestModal.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.security.sasl.AuthenticationException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class AuthService {
    final UserRepository userRepository;
    final RestTemplate restTemplate;

    HashMap<String, String> fakeDB;

    @Autowired
    public AuthService(UserRepository userRepository, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;

        fakeDB = new HashMap<>();
        int roleLength = UserRole.values().length;
        IntStream.range(roleLength, roleLength + 10).forEach(i -> {
            fakeDB.put(i + "@gmail.com", "user" + i);
        });
    }

    public User createUser(UserRequest userRequest) {
        Optional<User> emailCheck = userRepository.findByEmail(userRequest.getEmail());
        if (emailCheck.isPresent())
            throw new DataIntegrityViolationException("Email already exists");

        User user = new User();
        user.setActive(userRequest.isActive());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setRole(userRequest.getRole());
        return userRepository.save(user);
    }

    public OAuthResponse OAuthVerify(String token) throws AuthenticationException {
        String apiUrl = "https://id.dev.peplink.com/auth/realms/dev/protocol/openid-connect/userinfo";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.getBody(), OAuthResponse.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new AuthenticationException();
        }
    }

    public User login(String token) throws AuthenticationException {
        OAuthResponse oauthUser = OAuthVerify(token);
        User user = userRepository.findByEmail(oauthUser.getEmail()).orElseThrow(AuthenticationException::new);
        if (user.isFirstLogin()) {
            user.setPicture(oauthUser.getPicture());
            user.setFirstLogin(false);
            return userRepository.save(user);
        }
        return user;
    }

    public Slice<User> getUsers(String search, String sort, String sortMode, int page, int size,
                                Map<String, String> params) {
        Specification<User> spec = Filter.getFilterSearch(User.class, params, search);
        return Filter.getSlicePage(userRepository, spec, sort, sortMode, page, size);
    }

    public User editUser(Long userId, UserRequest request) {
        User user = getUserById(userId);
        Optional<User> emailCheck = userRepository.findByEmail(request.getEmail());
        if (emailCheck.isPresent() && !Objects.equals(emailCheck.get().getId(), user.getId())) {
            throw new DataIntegrityViolationException("Email already exists");
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setActive(request.isActive());
        user.setRole(request.getRole());
        return userRepository.save(user);
    }

    User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid User id " + +id));
    }

    // region Initialize
    public void createFakeData() {
        System.out.println("creating Fake User Data");
        AtomicInteger counter = new AtomicInteger();
        List<User> users = Stream.generate(() -> {
                    int i = counter.getAndIncrement();
                    User user = new User();
                    user.setFirstName(UserRole.values()[i].toString());
                    user.setEmail(i + "@gmail.com");
                    user.setRole(UserRole.values()[i]);
                    return user;
                }).limit(UserRole.values().length)
                .collect(Collectors.toList());

        User user = new User();
        user.setFirstName("Wung");
        user.setLastName("Lai");
        user.setEmail("opfk00@gmail.com");
        user.setRole(UserRole.SuperAdmin);
        users.add(user);

        User user1 = new User();
        user1.setFirstName("Zac");
        user1.setLastName("Lai");
        user1.setEmail("zaclai02@gmail.com");
        user1.setRole(UserRole.SuperAdmin);
        users.add(user1);

        User user2 = new User();
        user2.setFirstName("Ah");
        user2.setLastName("Wung");
        user2.setEmail("ahwungteen@gmail.com");
        user2.setRole(UserRole.SuperAdmin);
        users.add(user2);

        User user3 = new User();
        user3.setFirstName("Yan");
        user3.setLastName("Chung");
        user3.setEmail("devs1.sf@gmail.com");
        user3.setRole(UserRole.SuperAdmin);
        users.add(user3);

        userRepository.saveAll(users);
    }
    // endregion
}
