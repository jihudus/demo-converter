CREATE TABLE IF NOT EXISTS files (
  id UUID NOT NULL,
   filename VARCHAR(255),
   processing BOOLEAN,
   last_operation BOOLEAN,
   CONSTRAINT pk_files PRIMARY KEY (id)
);