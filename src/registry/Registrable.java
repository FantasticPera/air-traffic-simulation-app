package registry;

import java.util.List;
import java.util.Optional;

public interface Registrable<ID, T> {
	
	void save(T item);
	void remove(ID id);
	T find(ID id);
	List<T> findAll();
	boolean exists(ID id);

}
