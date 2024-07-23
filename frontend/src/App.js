import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import MainPage from './components/MainPage';
import OutputPage from './components/OutputPage';

function App() {
  return (
      <Router>
        <Routes>
          <Route path="/" element={<MainPage />} />
          <Route path="/results" element={<OutputPage />} />
        </Routes>
      </Router>
  );
}

export default App;
