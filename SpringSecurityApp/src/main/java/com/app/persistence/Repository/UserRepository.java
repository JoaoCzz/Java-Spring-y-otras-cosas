package com.app.persistence.Repository;

import com.app.persistence.Entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Repositorio para la entidad {@link UserEntity}.
 * <p>
 * Proporciona métodos de acceso a datos relacionados con usuarios,
 * extendiendo la funcionalidad de {@link org.springframework.data.jpa.repository.JpaRepository}.
 * </p>
 *
 * <p>Spring Data JPA genera automáticamente la implementación de los métodos
 * basándose en su nombre, sin necesidad de escribir consultas manuales.</p>
 */
@Repository
public interface UserRepository extends CrudRepository<UserEntity,Long> {

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * <p>Spring Data JPA interpreta el nombre del método
     * y genera automáticamente la consulta equivalente a:</p>
     * <pre>
     * SELECT u FROM UserEntity u WHERE u.username = :username
     * </pre>
     *
     * @param username el nombre de usuario a buscar.
     * @return un {@link Optional} que contiene el {@link UserEntity} si se encuentra,
     *         o vacío si no existe un usuario con ese nombre.
     */
    Optional<UserEntity> findUserEntityByUsername(String username);

    // Ejemplo alternativo con consulta personalizada (comentado):
    // Permite definir manualmente la consulta JPQL.
    // @Query("SELECT u FROM UserEntity u WHERE u.username = ?1")
    // Optional<UserEntity> findUser(String username);
}
