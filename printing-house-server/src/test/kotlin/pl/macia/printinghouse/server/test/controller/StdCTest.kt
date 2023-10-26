package pl.macia.printinghouse.server.test.controller

import com.jayway.jsonpath.JsonPath
import kotlinx.serialization.json.Json
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import pl.macia.printinghouse.response.RecID

/**
 * Standard controller test.
 */
class StdCTest(
    private val url: String,
    private val webApplicationContext: WebApplicationContext,
    private val nonExistingId: Int = 9999
) {
    internal lateinit var mvc: MockMvc

    fun beforeEach() {
        mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
            .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
            .build()
    }

    fun checkGetAllFromPath(vararg matchers: ResultMatcher) {
        mvc.perform(MockMvcRequestBuilders.get(url))
            .andExpect { status().isOk }
            .andExpectAll(*matchers)
    }

    fun checkFindOneById(
        existingId: Int,
        vararg matchers: ResultMatcher,
        idTempl: String = "{id}"
    ) {
        mvc.perform(MockMvcRequestBuilders.get("$url/$idTempl", existingId))
            .andExpect { status().isOk }
            .andExpectAll(
                *matchers,
            )
        mvc.perform(MockMvcRequestBuilders.get("$url/$idTempl", nonExistingId))
            .andExpect { status().isNotFound }
    }

    fun checkInsertOneObj(
        encodedJson: String,
        idJName: String,
        vararg moreMatchers: ResultMatcher,
        idTempl: String = "{id}"
    ): RecID {
        val respIdField = RecID::id.name
        val res = mvc.perform(
            MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON)
                .content(encodedJson)
        ).andExpect(status().isOk)
            .andReturn()

        val response: String = res.response.contentAsString
        val id: Int = JsonPath.parse(response).read("$.$respIdField")
        mvc.perform(MockMvcRequestBuilders.get("$url/$idTempl", id))
            .andExpectAll(
                status().isOk,
                jsonPath("$.$idJName").value(id),
                *moreMatchers
            )

        return Json.decodeFromString<RecID>(res.response.contentAsString)
    }

    fun checkDeleteObjTest(newRecId: RecID, idTempl: String = "{id}") {
        mvc.perform(
            MockMvcRequestBuilders.delete("$url/$idTempl", newRecId.asInt())
        ).andExpect(status().isOk)

        mvc.perform(
            MockMvcRequestBuilders.get("$url/$idTempl", newRecId.asInt())
        ).andExpect(status().isNotFound)
    }

    fun checkChangeObjTest(
        changeId: Int,
        jsonChangeReq: String,
        vararg matchers: ResultMatcher,
        idTempl: String = "{id}"
    ) {
        mvc.perform(
            MockMvcRequestBuilders.put("$url/$idTempl", changeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonChangeReq)
        ).andExpectAll(
            status().isOk,
            jsonPath("$.changed").value(true)
        )
        mvc.perform(
            MockMvcRequestBuilders.get("$url/$idTempl", changeId)
        ).andExpectAll(
            status().isOk,
            *matchers
        )

        mvc.perform(
            MockMvcRequestBuilders.put("$url/$idTempl", changeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonChangeReq)
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.changed").value(false))

        mvc.perform(
            MockMvcRequestBuilders.put("$url/$idTempl", nonExistingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonChangeReq)
        ).andExpect(status().isNotFound)
    }
}