package com.app.persistence.Repository;

import com.app.persistence.Entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Repositorio para la entidad UserEntity.
 *
 * Proporciona métodos de acceso a datos relacionados con los usuarios,
 * extendiendo la funcionalidad de JpaRepository o CrudRepository.
 *
 * Spring Data JPA genera automáticamente la implementación de los métodos
 * basándose en su nombre, sin necesidad de escribir consultas manuales.
 */
@Repository
public interface UserRepository extends CrudRepository<UserEntity,Long> {

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * Spring Data JPA interpreta el nombre del mét0do
     * y genera automáticamente la consulta equivalente a:
     *
     *   SELECT u FROM UserEntity u WHERE u.username = :username
     *
     * @param username nombre de usuario a buscar.
     * @return un Optional con la entidad UserEntity si se encuentra,
     *         o vacío si no existe un usuario con ese nombre.
     */
    Optional<UserEntity> findUserEntityByUsername(String username);

    // Ejemplo alternativo con consulta personalizada (comentado):
    // Permite definir manualmente la consulta JPQL.
    // @Query("SELECT u FROM UserEntity u WHERE u.username = ?1")
    // Optional<UserEntity> findUser(String username);
}
