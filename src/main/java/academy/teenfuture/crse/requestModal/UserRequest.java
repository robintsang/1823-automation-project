package academy.teenfuture.crse.requestModal;

import academy.teenfuture.crse.Enum.UserRole;
import lombok.Data;

@Data
public class UserRequest {
  boolean active;
  String firstName;
  String lastName;
  String email;
  UserRole role;
}
