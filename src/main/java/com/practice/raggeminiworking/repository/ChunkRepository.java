package com.practice.raggeminiworking.repository;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Collectors;

import com.practice.raggeminiworking.model.VectorChunk;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChunkRepository {

    private final JdbcTemplate jdbcTemplate;

    public ChunkRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveChunk(VectorChunk chunk) {

        String vectorString = chunk.getEmbedding()
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",", "[", "]"));

        String sql = """
                INSERT INTO vector_chunks(
                    session_id,
                    source_type,
                    file_name,
                    text,
                    embedding
                )
                VALUES (?, ?, ?, ?, ?::vector)
                """;

        jdbcTemplate.update(
                sql,
                chunk.getSessionId(),
                chunk.getSourceType(),
                chunk.getFileName(),
                chunk.getText(),
                vectorString
        );
    }
    
    public void saveAllChunks(List<VectorChunk> chunks) {
        String sql = """
                INSERT INTO vector_chunks(
                    session_id,
                    source_type,
                    file_name,
                    text,
                    embedding
                )
                VALUES (?, ?, ?, ?, ?::vector)
                """;

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                VectorChunk chunk = chunks.get(i);
                ps.setString(1, chunk.getSessionId());
                ps.setString(2, chunk.getSourceType());
                ps.setString(3, chunk.getFileName());
                ps.setString(4, chunk.getText());

                String vectorString = chunk.getEmbedding().stream()
                        .map(String::valueOf).collect(Collectors.joining(",", "[", "]"));
                ps.setString(5, vectorString);
            }

            @Override
            public int getBatchSize() {
                return chunks.size();
            }
        });
    }

    public void deleteAllChunks() {

        String sql = "DELETE FROM vector_chunks";

        jdbcTemplate.update(sql);

        System.out.println("Old vectors deleted.");
    }


    public List<VectorChunk> searchSimilarChunks(String sessionId, String question, String embedding, int limit) {

        String sql = """
        SELECT id, session_id, text
        FROM vector_chunks
        WHERE session_id = ?
        ORDER BY embedding <=> ?::vector
        LIMIT ?
        """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    VectorChunk chunk = new VectorChunk(
                            rs.getLong("id"),
                            rs.getString("session_id"),
                            rs.getString("text"),
                            null
                    );
                    return chunk;
                },
                sessionId,
                embedding,
                limit
        );
    }
}