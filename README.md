# Keyword_counter

Concurrent and distributed systems - homework 1

This project was conceived as the implementation of a system for counting occurrences of keywords in different types of corpuses.

Keyword Counting: The main purpose of the system is to count the occurrences of certain keywords within various types of corpora, including ASCII-encoded text files.

Concurrent Processing: The system should be concurrent, meaning it can process multiple corpuses or jobs at the same time without blocking.

Easy addition of new components: It is possible to add new system components relatively easily. This allows system extensibility for future requirements.

Robustness and fault management: The system is fault tolerant and never collapse completely. Errors are handled gracefully, and the user will be notified of problems.

Command Line Interaction (CLI): The user is able to interact with the system via the command line, where they can enter commands to add corpus, perform searches, and other actions.

Configuration via configuration file: System settings can be read from a configuration file, allowing customization of the system without the need to change the source code.
