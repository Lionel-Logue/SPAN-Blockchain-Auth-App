import { getSession, signOut } from 'next-auth/react';
//getSession function retrieves the session object for the current user. 
//The session object contains information about the user’s authentication status and any additional data that was returned by the authentication provider.
//The signOut redirects the user back to main page
//JSON returns the information about the user session including profileId, address and other parameters
function User({ user }) {
    return (
        <div>
            <h4>User session:</h4>
            <pre>{JSON.stringify(user, null, 2)}</pre>
            <button onClick={() => signOut({ redirect: '/' })}>Sign out</button>
        </div>
    );
}

export async function getServerSideProps(context) {
    const session = await getSession(context);

    if (!session) {
        return {
            redirect: {
                destination: '/',
                permanent: false,
            },
        };
    }

    return {
        props: { user: session.user },
    };
}

export default User;
