# spring-restdocs-example


# Spring REST docs �T���v��
Spring REST docs�̓��j�b�g�e�X�g�̃��N�G�X�g�E���X�|���X���e��asciidoc�ƘA�g���AAPI�d�l���Ƃ��ďo�͂���ׂ̃��C�u�����ł��B 

# �\�[�X����

## (�e�X�g�R�[�h)[src/test/java/sample/restdocs/SpringRestdocsApplicationTests.java]

1.�N���X�A�m�e�[�V�����Ɉȉ���ǉ��B
target/generated-snippets��.adoc�t�@�C���̏o�͐�. 

  * @ExtendWith({ RestDocumentationExtension.class, SpringExtension.class }) 
  * @AutoConfigureRestDocs(outputDir = "target/generated-snippets") 

2.WebApplicationContext�Ɉȉ���Configuration��K�p(apply)���� 

  * documentationConfiguration(restDocumentation) 

3.�e�e�X�g���̏������Ƀ��N�G�X�g���̃R�}���h��A���N�G�X�g����у��X�|���X�̃{�f�B����andDo���g���ē��e���o�͂���B  mockMvc.perform(post("/boards").content(boardsJson).contentType("application/json"))
  .andDo(print())  // ���N�G�X�g����curl�R�}���h�̓��e���o��
  .andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().json(boardsJson))
  .andDo(document("{methodName}",    // ���\�b�h���̏o�͂ƂȂ� 
      preprocessRequest(prettyPrint()), �@// ���N�G�X�g
      preprocessResponse(prettyPrint())));} // ���X�|���X

## (�d�l���e���v���[�g)[src/main/asciidoc/index.adoc]
1.src/main/asciidoc/index.adoc �t�@�C����p�ӂ��A�h�L�������g�ƃ}�[�W����Badoc�t�@�C����Markdown�Ɏ����L�@���g���ď����B 
index.adoc�ɂ�API�d�l�̃K�C�h���C�������L�ڂ��A�o�͗�Ƃ��āA�e�X�g�P�[�X�̐���n��ُ�n�̃��X�|���X���ʂ̗���܂߂ăh�L�������g���o�͂���B 


