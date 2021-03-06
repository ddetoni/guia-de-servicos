package br.gov.servicos.orgao;

import br.gov.servicos.busca.Buscador;
import br.gov.servicos.cms.Markdown;
import br.gov.servicos.servico.Servico;
import lombok.experimental.FieldDefaults;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;

import java.util.List;

import static br.gov.servicos.fixtures.TestData.CONTEUDO_HTML;
import static br.gov.servicos.fixtures.TestData.SERVICO;
import static java.util.Collections.singletonList;
import static java.util.Optional.of;
import static lombok.AccessLevel.PRIVATE;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.ModelAndViewAssert.assertCompareListModelAttribute;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeValue;

@RunWith(MockitoJUnitRunner.class)
@FieldDefaults(level = PRIVATE)
public class OrgaoControllerTest {

    @Mock
    Buscador buscador;

    @Mock
    Markdown markdown;

    @Mock
    OrgaoRepository orgaos;

    List<Servico> umServico = singletonList(SERVICO);
    OrgaoController controller;

    @Before
    public void setUp() {
        controller = new OrgaoController(buscador, markdown, orgaos);
    }

    @Test
    public void exibicaoDeLinhaDaVidaRetornaServicos() {
        given(markdown.toHtml(anyObject())).willReturn(CONTEUDO_HTML);

        doReturn(umServico)
                .when(buscador)
                .buscaSemelhante(of("receita-federal"), "prestador.id", "responsavel.id");

        assertCompareListModelAttribute(controller.orgao("receita-federal"), "resultados", umServico);
    }

    @Test
    public void exibicaoDeLinhaDaVidaRetornaConteudoDescritivo() {
        doReturn(CONTEUDO_HTML)
                .when(markdown)
                .toHtml(new ClassPathResource("conteudo/orgaos/secretaria-da-receita-federal-do-brasil-rfb.md"));

        assertModelAttributeValue(controller.orgao("secretaria-da-receita-federal-do-brasil-rfb"), "conteudo", CONTEUDO_HTML.withId("secretaria-da-receita-federal-do-brasil-rfb"));
    }

}
