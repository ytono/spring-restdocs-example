# spring-restdocs-example


# Spring REST docs サンプル
Spring REST docsはユニットテストのリクエスト・レスポンス内容をasciidocと連携し、API仕様書として出力する為のライブラリです。 

# ソース説明

## テストコード 
src/test/java/sample/restdocs/SpringRestdocsApplicationTests.java [コード](src/test/java/sample/restdocs/SpringRestdocsApplicationTests.java)

1. クラスアノテーションに以下を追加。
  target/generated-snippetsは.adocファイルの出力先. 

    ```
    @ExtendWith({ RestDocumentationExtension.class, SpringExtension.class }) 
    @AutoConfigureRestDocs(outputDir = "target/generated-snippets") 
    ```
2. WebApplicationContextに以下のConfigurationを適用(apply)する 

    ```
    .apply(documentationConfiguration(restDocumentation)).build() 
    ```

3. 各テスト内の処理中にリクエスト時のコマンドや、リクエストおよびレスポンスのボディ部のandDoを使って内容を出力する。  

    ```
    mockMvc.perform(post("/boards").content(boardsJson).contentType("application/json"))
    .andDo(print())  // リクエスト時のcurlコマンドの内容を出力
    .andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().json(boardsJson))
    .andDo(document("{methodName}",    // メソッド名の出力となる 
        preprocessRequest(prettyPrint()), 　// リクエスト
        preprocessResponse(prettyPrint())));} // レスポンス
    ```

## 仕様書テンプレート
src/main/asciidoc/index.adoc [コード](src/main/asciidoc/index.adoc)

1. src/main/asciidoc/index.adocドキュメントとマージする。  
adocファイルはMarkdownに似た記法で書かれたもので、API仕様のガイドライン等を記載し、上記スニペットに出力されたテストケースの正常系や異常系のレスポンス結果の例をリンクすることにより、ドキュメントの連携ができる。 

    ```
    == POST API Example

    .request
    include::{snippets}/testAddBoards/http-request.adoc[]

    .response
    include::{snippets}/testAddBoards/http-response.adoc[]
    ```

## 出力先
　./target/generated-docs/index.html

## 出力例
  ./sample.html
