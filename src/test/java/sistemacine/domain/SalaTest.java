package sistemacine.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sistemacine.web.rest.TestUtil;

class SalaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sala.class);
        Sala sala1 = new Sala();
        sala1.setId("id1");
        Sala sala2 = new Sala();
        sala2.setId(sala1.getId());
        assertThat(sala1).isEqualTo(sala2);
        sala2.setId("id2");
        assertThat(sala1).isNotEqualTo(sala2);
        sala1.setId(null);
        assertThat(sala1).isNotEqualTo(sala2);
    }
}
