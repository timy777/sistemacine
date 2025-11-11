package sistemacine.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sistemacine.web.rest.TestUtil;

class PersonaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonaDTO.class);
        PersonaDTO personaDTO1 = new PersonaDTO();
        personaDTO1.setId("id1");
        PersonaDTO personaDTO2 = new PersonaDTO();
        assertThat(personaDTO1).isNotEqualTo(personaDTO2);
        personaDTO2.setId(personaDTO1.getId());
        assertThat(personaDTO1).isEqualTo(personaDTO2);
        personaDTO2.setId("id2");
        assertThat(personaDTO1).isNotEqualTo(personaDTO2);
        personaDTO1.setId(null);
        assertThat(personaDTO1).isNotEqualTo(personaDTO2);
    }
}
