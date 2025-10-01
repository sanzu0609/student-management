import { useEffect, useState } from "react";
import "./App.css";

const initialForm = {
  fullName: "",
  email: "",
  major: ""
};

function App() {
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [form, setForm] = useState(initialForm);
  const [submitting, setSubmitting] = useState(false);
  const [notification, setNotification] = useState(null);
  const [deletingId, setDeletingId] = useState(null);

  useEffect(() => {
    const loadStudents = async () => {
      try {
        setError(null);
        const response = await fetch("/api/students");
        if (!response.ok) {
          throw new Error(`Unable to load students (status ${response.status})`);
        }
        const data = await response.json();
        data.sort((a, b) => a.id - b.id);
        setStudents(data);
      } catch (err) {
        setError(err.message ?? "Something went wrong while loading students");
      } finally {
        setLoading(false);
      }
    };

    loadStudents();
  }, []);

  useEffect(() => {
    if (!notification) {
      return;
    }
    const timeout = window.setTimeout(() => setNotification(null), 4000);
    return () => window.clearTimeout(timeout);
  }, [notification]);

  const handleChange = (event) => {
    const { name, value } = event.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    if (!form.fullName.trim() || !form.email.trim() || !form.major.trim()) {
      setNotification("Please fill out all fields before submitting.");
      setError(null);
      return;
    }

    setSubmitting(true);
    try {
      const response = await fetch("/api/students", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(form)
      });

      if (!response.ok) {
        const message = await response.text();
        throw new Error(message || `Failed to create student (status ${response.status})`);
      }

      const created = await response.json();
      setStudents((prev) => [...prev, created].sort((a, b) => a.id - b.id));
      setForm(initialForm);
      setNotification(`Added ${created.fullName}.`);
      setError(null);
    } catch (err) {
      setNotification(null);
      setError(err.message ?? "Unable to create student");
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async (id) => {
    const student = students.find((item) => item.id === id);
    if (!student) {
      return;
    }

    const confirmed = window.confirm(`Remove ${student.fullName}?`);
    if (!confirmed) {
      return;
    }

    setDeletingId(id);
    try {
      const response = await fetch(`/api/students/${id}`, {
        method: "DELETE"
      });

      if (!response.ok) {
        const message = await response.text();
        throw new Error(message || `Failed to delete student (status ${response.status})`);
      }

      setStudents((prev) => prev.filter((item) => item.id !== id));
      setNotification(`Removed ${student.fullName}.`);
      setError(null);
    } catch (err) {
      setNotification(null);
      setError(err.message ?? "Unable to delete student");
    } finally {
      setDeletingId(null);
    }
  };

  return (
    <div className="page">
      <div className="container">
        <header>
          <h1>Student Management</h1>
          <p>React SPA bundled inside Spring Boot.</p>
        </header>

        {notification && <div className="alert success">{notification}</div>}

        <section className="card">
          <h2>Add Student</h2>
          <p className="card-description">
            Capture a new student profile. Validation runs on both the UI and the Spring Boot API.
          </p>
          <form className="student-form" onSubmit={handleSubmit} noValidate>
            <div className="form-grid">
              <label className="field">
                <span className="field-label">Full name</span>
                <input
                  name="fullName"
                  type="text"
                  placeholder="Nguyen Van A"
                  value={form.fullName}
                  onChange={handleChange}
                  disabled={submitting}
                  required
                />
              </label>
              <label className="field">
                <span className="field-label">Email</span>
                <input
                  name="email"
                  type="email"
                  placeholder="student@example.com"
                  value={form.email}
                  onChange={handleChange}
                  disabled={submitting}
                  required
                />
              </label>
              <label className="field">
                <span className="field-label">Major</span>
                <input
                  name="major"
                  type="text"
                  placeholder="Software Engineering"
                  value={form.major}
                  onChange={handleChange}
                  disabled={submitting}
                  required
                />
              </label>
            </div>
            <button type="submit" className="button" disabled={submitting}>
              {submitting ? "Saving..." : "Add student"}
            </button>
          </form>
        </section>

        <section className="card">
          <div className="card-header">
            <h2>Student Directory</h2>
            {!loading && !error && (
              <span className="badge">{students.length} total</span>
            )}
          </div>

          {loading && <p className="muted">Loading students...</p>}
          {!loading && error && <div className="alert error">{error}</div>}

          {!loading && !error && students.length === 0 && (
            <p className="muted">No students yet. Add the first one with the form above.</p>
          )}

          {!loading && !error && students.length > 0 && (
            <div className="table-wrapper">
              <table className="students-table">
                <thead>
                  <tr>
                    <th scope="col">ID</th>
                    <th scope="col">Name</th>
                    <th scope="col">Email</th>
                    <th scope="col">Major</th>
                    <th scope="col" className="actions">Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {students.map((student) => (
                    <tr key={student.id}>
                      <td>{student.id}</td>
                      <td>
                        <strong>{student.fullName}</strong>
                      </td>
                      <td>
                        <a href={`mailto:${student.email}`} className="link">
                          {student.email}
                        </a>
                      </td>
                      <td>{student.major}</td>
                      <td className="actions">
                        <button
                          type="button"
                          className="button-secondary"
                          onClick={() => handleDelete(student.id)}
                          disabled={deletingId === student.id}
                        >
                          {deletingId === student.id ? "Removing..." : "Remove"}
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </section>
      </div>
    </div>
  );
}

export default App;