import './Navbar.css';

interface NavbarProps {
  currentPage?: 'home' | 'modules' | 'students' | 'grades';
}

function Navbar({ currentPage }: NavbarProps) {
  const isModules = currentPage === 'modules';
  const isStudents = currentPage === 'students';
  const isGrades = currentPage === 'grades';

  return (
    <header className="navbar">
      <div className="navbar-content">
        <div className="navbar-title">
          {/* Always link "Awesome University" to home page */}
          <a href="/" className="title-link">
            Awesome University
          </a>
        </div>
        <nav className="navbar-links">
          {isModules ? (
            <span className="nav-link active">Modules</span>
          ) : (
            <a href="/modules" className="nav-link">
              Modules
            </a>
          )}
          {isStudents ? (
            <span className="nav-link active">Students</span>
          ) : (
            <a href="/students" className="nav-link">
              Students
            </a>
          )}
          {isGrades ? (
            <span className="nav-link active">Grades</span>
          ) : (
            <a href="/grades" className="nav-link">
              Grades
            </a>
          )}
        </nav>
      </div>
    </header>
  );
}

export default Navbar;
