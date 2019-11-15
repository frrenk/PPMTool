import React, { Component } from "react";
import { Link } from "react-router-dom";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { logout } from "../../actions/securityActions";

class Header extends Component {
  logout() {
    this.props.logout();
    window.location.href = "/";
  }
  render() {
    const { validToken, user } = this.props.security;
    const userIsAuthenticated = (
      <ul className="navbar-nav ml-auto">
        <li className="nav-item">
          <Link className="nav-link " to="/dashboard">
            <i className="fas fa-user-circle mr1" />
            {user.fullName}
          </Link>
        </li>
        <li className="nav-item">
          <Link className="nav-link" to="/" onClick={this.logout.bind(this)}>
            Logout
          </Link>
        </li>
      </ul>
    );

    const brandAuthenticated = (
      <Link className="navbar-brand" to="/dashboard">
        Personal Project Management Tool
      </Link>
    );

    const userIsNotAuthenticated = (
      <div className="collapse navbar-collapse" id="mobile-nav">
        <ul className="navbar-nav ml-auto">
          <li className="nav-item">
            <Link className="nav-link" to="/register">
              Sign Up
            </Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/login">
              Login
            </Link>
          </li>
        </ul>
      </div>
    );

    const brandNotAuthenticated = (
      <Link className="navbar-brand" to="">
        Personal Project Management Tool
      </Link>
    );

    let headerLinks;
    let brand;

    if (validToken && user) {
      headerLinks = userIsAuthenticated;
      brand = brandAuthenticated;
    } else {
      headerLinks = userIsNotAuthenticated;
      brand = brandNotAuthenticated;
    }

    return (
      <nav className="navbar navbar-expand-sm navbar-dark bg-primary mb-4">
        <div className="container">
          {brand}
          <button
            className="navbar-toggler"
            type="button"
            data-toggle="collapse"
            data-target="#mobile-nav"
          >
            <span className="navbar-toggler-icon" />
          </button>
          <div className="collapse navbar-collapse" id="mobile-nav">
            {headerLinks}
          </div>
        </div>
      </nav>
    );
  }
}

Header.propTypes = {
  logout: PropTypes.func.isRequired,
  errors: PropTypes.object.isRequired,
  security: PropTypes.object.isRequired
};

const mapStateToProps = state => ({
  security: state.security,
  errors: state.errors
});

export default connect(mapStateToProps, { logout })(Header);
