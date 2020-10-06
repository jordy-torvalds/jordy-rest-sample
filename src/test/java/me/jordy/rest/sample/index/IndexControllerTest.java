package me.jordy.rest.sample.index;

import me.jordy.rest.sample.common.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class IndexControllerTest extends BaseTest {

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
