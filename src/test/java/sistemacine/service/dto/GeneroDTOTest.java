package sistemacine.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sistemacine.web.rest.TestUtil;

class GeneroDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GeneroDTO.class);
        GeneroDTO generoDTO1 = new GeneroDTO();
        generoDTO1.setId("id1");
        GeneroDTO generoDTO2 = new GeneroDTO();
        assertThat(generoDTO1).isNotEqualTo(generoDTO2);
        generoDTO2.setId(generoDTO1.getId());
        assertThat(generoDTO1).isEqualTo(generoDTO2);
        generoDTO2.setId("id2");
        assertThat(generoDTO1).isNotEqualTo(generoDTO2);
        generoDTO1.setId(null);
        assertThat(generoDTO1).isNotEqualTo(generoDTO2);
    }
}
