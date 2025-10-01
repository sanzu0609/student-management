import { useEffect, useState } from "react";
import "./App.css";

function App() {
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetch("/api/students")
      .then((response) => {
        if (!response.ok) {
          throw new Error(`Request failed with status ${response.status}`);
        }
        return response.json();
      })
      .then((data) => {
        setStudents(data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  }, []);

  return (
    <div className="container">
      <header>
        <h1>Student Management</h1>
        <p>React SPA served from Spring Boot</p>
      </header>

      {loading && <p>Loading students...</p>}
      {error && <p className="error">{error}</p>}

      {!loading && !error && (
        <ul className="student-list">
          {students.map((student) => (
            <li key={student}>{student}</li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default App;
