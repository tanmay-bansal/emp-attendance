package  com.vibrent.empattendance.empattendance.models;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FooDto implements Serializable {
  private static final long serialVersionUID = -6949375489579665L;
  private String key;
  private String value;
}
