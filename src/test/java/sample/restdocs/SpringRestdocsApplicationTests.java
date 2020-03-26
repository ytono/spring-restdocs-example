package sample.restdocs;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import sample.restdocs.entity.Board;
import sample.restdocs.repository.BoardRepository;

/**
 * Spring REST docs の使い方<br>
 * Spring REST docsはユニットテストのリクエスト・レスポンス内容をasciidocと連携し、API仕様書として出力する為のライブラリです。
 * <ol>
 * <li>クラスアノテーションに以下を追加。target/generated-snippetsは.adocファイルの出力先.
 * <ul>
 * <li>@ExtendWith({ RestDocumentationExtension.class, SpringExtension.class })
 * <li>@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
 * </ul>
 * <li>WebApplicationContextに以下のConfigurationを適用(apply)する
 * <ul>
 * <li>documentationConfiguration(restDocumentation)
 * </ul>
 * <li>各テスト内の処理中にリクエスト時のコマンドや、リクエストおよびレスポンスのボディ部のandDoを使って内容を出力する。
 * <pre>
 * {@code mockMvc.perform(post("/boards").content(boardsJson).contentType("application/json"))
 *  .andDo(print())  // リクエスト時のcurlコマンドの内容を出力
 *  .andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().json(boardsJson))
 *  .andDo(document("{methodName}",    // メソッド名の出力となる 
 *      preprocessRequest(prettyPrint()), 　// リクエスト
 *      preprocessResponse(prettyPrint())));} // レスポンス
 * </pre>
 * <li>{@code src/main/asciidoc/index.adoc }ファイルを用意し、ドキュメントとマージする。adocファイルはMarkdownに似た記法を使って書く。
 * <p>
 * index.adocにはAPI仕様のガイドライン等を記載し、出力例として、テストケースの正常系や異常系のレスポンス結果の例を含めてドキュメントを出力する。
 * </ol>
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith({ RestDocumentationExtension.class, SpringExtension.class })
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
class SpringRestdocsApplicationTests {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private BoardRepository repository;

	private MockMvc mockMvc;

	List<Board> boards = null;

	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {

		/*
		 * ContextにdocumentationConfiguration(restDocumentation)を追加。
		 */
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.apply(documentationConfiguration(restDocumentation)).build();

		boards = Stream.of(new Board(1, "Java", 1, null, 1, null, false, 0),
				new Board(2, "Spring", 1, null, 1, null, false, 0)).collect(Collectors.toList());

		repository.saveAll(boards);

		boards = Stream.of(new Board(3, "Pivotal", 1, null, 1, null, false, 0),
				new Board(4, "VMware", 1, null, 1, null, false, 0)).collect(Collectors.toList());

	}

	@Test
	// @Order(1)
	public void testAddBoards() throws Exception {
		String boardsJson = new ObjectMapper().writeValueAsString(boards);
		/*
		 * .andDo(print()) 処理前の .andDo(document("{methodName}",
		 * preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
		 */
		mockMvc.perform(post("/boards").content(boardsJson).contentType("application/json")).andDo(print())
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().json(boardsJson))
				.andDo(document("{methodName}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
	}

	@Test
	// @Order(2)
	public void testGetBoards() throws Exception {
		String nowDate = new Timestamp(System.currentTimeMillis()).toString().substring(0, 10);
		String result1 = "$.[?(@.boardId == 1 && @.name == \"Java\" && @.createUserId == 1 && @.updateUserId == 1 && @.deleteFlg == false && @.version == 0)]";
		String result2 = "$.[?(@.boardId == 2 && @.name == \"Spring\" && @.createUserId == 1 && @.updateUserId == 1 && @.deleteFlg == false && @.version == 0)]";
// 日付は難しいいので、スキップ。
//		String result1 = "$.[?(@.boardId == 1 && @.name == \"Java\" && @.createUserId == 1 && @.createDate == \"" + nowDate
//				+ "\" && @.updateUserId == 1 && @.updateDate == \"" + nowDate + "\")]";

		mockMvc.perform(get("/boards").contentType("application/json")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath(result1).exists()).andExpect(jsonPath(result2).exists())
				.andDo(document("{methodName}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
		// 内部で追加する項目が無ければ...
		// .andExpect(MockMvcResultMatchers.content().json(new
		// ObjectMapper().writeValueAsString(boards)))
	}

	@Test
	// @Order(3)
	public void testGetBoardsbyId() throws Exception {
		String nowDate = new Timestamp(System.currentTimeMillis()).toString().substring(0, 10);
		String result1 = "$.[?(@.boardId == 1 && @.name == \"Java\" && @.createUserId == 1 && @.updateUserId == 1 && @.deleteFlg == false && @.version == 0)]";
		String result2 = "$.[?(@.boardId == 2 && @.name == \"Spring\" && @.createUserId == 1 && @.updateUserId == 1 && @.deleteFlg == false && @.version == 0)]";

		mockMvc.perform(get("/boards/1").contentType("application/json")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath(result1).exists())
				.andDo(document("{methodName}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
		mockMvc.perform(get("/boards/2").contentType("application/json")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath(result2).exists())
				.andDo(document("{methodName}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
	}

}
