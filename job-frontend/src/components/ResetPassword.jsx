import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate, useLocation } from 'react-router-dom';

const ResetPassword = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const token = new URLSearchParams(location.search).get('token'); // Extract token from the URL.

  const [formData, setFormData] = useState({ newPassword: '', confirmNewPassword: '' });
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');
  const [isTokenValid, setIsTokenValid] = useState(false);

  useEffect(() => {
    // Validate the token before allowing password reset form to load
    const validateToken = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/reset-password?token=${token}`);
        if (response.data.message === 'Token is valid.') {
          setIsTokenValid(true);
        } else {
          setError('Invalid or expired token.');
        }
      } catch (error) {
        setError('Token validation failed.');
      }
    };

    validateToken();
  }, [token]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setMessage('');

    // Validate password fields
    if (formData.newPassword !== formData.confirmNewPassword) {
      setError('Passwords do not match.');
      return;
    }

    if (!formData.newPassword || !formData.confirmNewPassword) {
      setError('Password fields cannot be empty.');
      return;
    }

    try {
      // Make API request to reset password
      const response = await axios.post('http://localhost:8080/api/reset-password', {
        token,
        newPassword: formData.newPassword,
        confirmNewPassword: formData.confirmNewPassword,
      });
      setMessage(response.data.message || 'Password reset successful!');
      
      // Redirect to login after 2 seconds
      setTimeout(() => {
        navigate('/login'); // Redirect to login page
      }, 2000);
    } catch (error) {
      setError(error.response?.data?.error || 'Password reset failed.');
    }
  };

  return (
    <div
      style={{
        maxWidth: '400px',
        margin: '50px auto',
        padding: '20px',
        borderRadius: '8px',
        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
        backgroundColor: '#ffffff',
        fontFamily: 'Arial, sans-serif',
        textAlign: 'center',
      }}
    >
      {isTokenValid ? (
        <>
          <h2
            style={{
              color: '#343a40',
              marginBottom: '20px',
              borderBottom: '2px solid #007bff',
              display: 'inline-block',
              paddingBottom: '5px',
            }}
          >
            Reset Password
          </h2>
          <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
            <input
              type="password"
              name="newPassword"
              placeholder="New Password"
              value={formData.newPassword}
              onChange={handleChange}
              required
              style={{
                padding: '10px',
                borderRadius: '4px',
                border: '1px solid #ced4da',
                fontSize: '16px',
                width: '100%',
              }}
            />
            <input
              type="password"
              name="confirmNewPassword"
              placeholder="Confirm New Password"
              value={formData.confirmNewPassword}
              onChange={handleChange}
              required
              style={{
                padding: '10px',
                borderRadius: '4px',
                border: '1px solid #ced4da',
                fontSize: '16px',
                width: '100%',
              }}
            />
            <button
              type="submit"
              style={{
                backgroundColor: '#007bff',
                color: '#ffffff',
                padding: '10px 20px',
                border: 'none',
                borderRadius: '4px',
                fontSize: '16px',
                cursor: 'pointer',
              }}
            >
              Reset Password
            </button>
          </form>
        </>
      ) : (
        <p
          style={{
            color: '#dc3545',
            fontWeight: 'bold',
            marginTop: '20px',
          }}
        >
          Invalid or expired token.
        </p>
      )}
      {error && (
        <p
          style={{
            color: '#dc3545',
            backgroundColor: '#f8d7da',
            padding: '10px',
            borderRadius: '4px',
            marginTop: '20px',
          }}
        >
          {error}
        </p>
      )}
      {message && (
        <p
          style={{
            color: '#28a745',
            backgroundColor: '#d4edda',
            padding: '10px',
            borderRadius: '4px',
            marginTop: '20px',
          }}
        >
          {message}
        </p>
      )}
    </div>
  );
};

export default ResetPassword;