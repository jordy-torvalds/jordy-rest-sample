package me.jordy.rest.sample.index;

import me.jordy.rest.sample.common.BaseControllerTest;
import me.jordy.rest.sample.common.RestDocsConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class IndexControllerTest extends BaseControllerTest {

    @Test
    public void index() throws Exception {
        mockMvc.perform(get("/api"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("_links.events").exists())
            .andDo(document("index",
                    links(
                            linkWithRel("events").description("link to event list")
                    ), responseHeaders (
                            headerWithName(HttpHeaders.CONTENT_TYPE).description("response data`s data type ")

                    ), responseFields (
                            fieldWithPath("_links.events.href").description("link to event list")
                    )
            ))
        ;

    }
}
