ALTER TABLE events ADD COLUMN reg_status_id BIGINT,
ADD CONSTRAINT fk_reg_status
        FOREIGN KEY (reg_status_id) REFERENCES registration_statuses (reg_status_id)