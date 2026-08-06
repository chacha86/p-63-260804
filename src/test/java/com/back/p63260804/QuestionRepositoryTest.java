package com.back.p63260804;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("findAll")
    void t1() {
        List<Question> all = this.questionRepository.findAll();
        // select * from question;

        assertEquals(2, all.size());

        Question q = all.get(0);
        assertEquals("sbb가 무엇인가요?", q.getSubject());
    }

    @Test
    @DisplayName("findById")
    void t2() {
        Optional<Question> oq = this.questionRepository.findById(1);
        // select * from question where id = 1
        if(oq.isPresent()) {
            Question q = oq.get();
            assertEquals("sbb가 무엇인가요?", q.getSubject());
        }
    }

    @Test
    @DisplayName("findBySubject")
    void t3() {
        Question q = this.questionRepository.findBySubject("sbb가 무엇인가요?").get();
        // select * from question where subject = 'sbb가 무엇인가요?'
        assertEquals(1, q.getId());
    }

    @Test
    @DisplayName("findBySubjectAndContent")
    void t4() {
        Question q = this.questionRepository.findBySubjectAndContent(
                "sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.").get();
        assertEquals(1, q.getId());
    }

    @Test
    @DisplayName("findBySubjectLike")
    void t5() {
        List<Question> qList = this.questionRepository.findBySubjectLike("sbb%");
        Question q = qList.get(0);
        assertEquals("sbb가 무엇인가요?", q.getSubject());
    }

    @Test
    @DisplayName("데이터 수정")
    void t6() {
        Optional<Question> oq = this.questionRepository.findById(1);
        assertTrue(oq.isPresent());
//        assertThat(oq.get().getSubject()).isEqualTo("sbb가 무엇인가요?");
        Question q = oq.get();
        q.setSubject("수정된 제목");

        this.questionRepository.save(q);

        Question q2 = this.questionRepository.findById(1).get();
        assertEquals("수정된 제목", q2.getSubject());

    }

    @Test
    @DisplayName("데이터 삭제")
    void t7() {

        assertEquals(2, this.questionRepository.count());
        Optional<Question> oq = this.questionRepository.findById(1);
        assertTrue(oq.isPresent());
        Question q = oq.get();
        this.questionRepository.delete(q);
        assertEquals(1, this.questionRepository.count());
    }

    @Test
    @DisplayName("답변 데이터 생성")
    @Transactional
    void t8() {
        Question question = this.questionRepository.findById(2).get();

        Answer a = new Answer();
        a.setContent("네 자동으로 생성됩니다.");
        a.setQuestion(question);  // 어떤 질문의 답변인지 알기위해서 Question 객체가 필요하다.
        a.setCreateDate(LocalDateTime.now());
        this.answerRepository.save(a);
    }

    @Test
    @DisplayName("답변 데이터 생성2 - OneToMany 방식")
    @Transactional
    void t9() {
        Question question2 = questionRepository.findById(2).get();

        Answer answer = new Answer();
        answer.setContent("답변 내용");
        answer.setQuestion(question2);
        answer.setCreateDate(LocalDateTime.now());

        question2.getAnswers().add(answer);
        questionRepository.flush(); // flush()를 호출하여 변경 내용을 데이터베이스에 즉시 반영

        Answer foundedAnswer = answerRepository.findById(answer.getId()).get(); // 데이터베이스에 저장되면서 answer Id 확보 가능.
        assertEquals("답변 내용", foundedAnswer.getContent());
    }

    @Test
    @DisplayName("답변 데이터 생성3 - Question의 addAnswer() 메서드 사용. 가장 객체지향적")
    @Transactional
    @Rollback(false)
    void t10() {
        Question question2 = questionRepository.findById(2).get();

        Answer answer = question2.addAnswer("답변 내용");
        questionRepository.flush();

        Answer answer2 = answerRepository.findById(answer.getId()).get();
        assertEquals("답변 내용", answer2.getContent());
    }

    @Test
    @DisplayName("EAGER 로딩")
    void t11() {
        Question question = questionRepository.findById(2).get();
        System.out.println(question.getAnswers().get(0).getContent());
    }

    @Test
    @DisplayName("LAZY 로딩")
    @Transactional
    void t12() {
        Question question = questionRepository.findById(2).get(); // 트랜잭션이 여기서 시작해서 여기서 닫힘
        System.out.println(question.getAnswers().get(0).getContent());
    }
}
