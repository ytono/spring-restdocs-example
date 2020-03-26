package sample.restdocs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import sample.restdocs.entity.Board;
import sample.restdocs.repository.BoardRepository;

@SpringBootApplication
public class SpringRestdocsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRestdocsApplication.class, args);
	}

	@RestController
	private static class RestContrller {
		@Autowired
		private BoardRepository repository;

		@PostMapping("/boards")
		public List<Board> saveBoard(@RequestBody List<Board> boards) {
			repository.saveAll(boards);
			return boards;
		}

		@GetMapping("/boards")
		public List<Board> getBoards() {
			return repository.findAll();
		}

		@GetMapping("/boards/{id}")
		public Board getBoards(@PathVariable("id") Integer id) {
			return repository.findById(id).orElse(null);
		}
	}
}
