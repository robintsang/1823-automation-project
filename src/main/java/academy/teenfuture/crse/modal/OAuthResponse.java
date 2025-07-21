package academy.teenfuture.crse.modal;

import lombok.Data;

@Data
public class OAuthResponse {
  String sub;
  boolean email_verified;
  String name;
  String preferred_username;
  String given_name;
  String family_name;
  String email;
  String picture;
}