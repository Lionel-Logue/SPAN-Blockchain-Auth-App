import { MetaMaskConnector } from 'wagmi/connectors/metaMask';//nedded to connect to MetaMask
import { signIn } from 'next-auth/react';//used to allow sign in flow by redirecting upon seccessful sign in
import { useAccount, useConnect, useSignMessage, useDisconnect } from 'wagmi';//needed to interact with MetaMask wallet
import { useRouter } from 'next/router';//allows navigation between pages
import { useAuthRequestChallengeEvm } from '@moralisweb3/next';//the Moralus hook for requesting a challenge message for authentication

//This defines a component that renders a button with an event listener that calls the function handleAuth() when clicked.
function SignIn() {
    const { connectAsync } = useConnect();
    const { disconnectAsync } = useDisconnect();
    const { isConnected } = useAccount();
    const { signMessageAsync } = useSignMessage();
    const { requestChallengeAsync } = useAuthRequestChallengeEvm();
    const { push } = useRouter();

    //This function first checks if there is an active connection and disconnects if there is one. 
    //Then it connects to MetaMask using the imported connector and requests a challenge message for authentication. 
    //After signing this message, it calls the imported signIn function and redirects to ‘/user’ page.
    const handleAuth = async () => {
        if (isConnected) {
            await disconnectAsync();
        }

        const { account, chain } = await connectAsync({ connector: new MetaMaskConnector() });

        const { message } = await requestChallengeAsync({ address: account, chainId: chain.id });

        const signature = await signMessageAsync({ message });

        // redirect user after successful authentication to '/user' page
        const { url } = await signIn('moralis-auth', { message, signature, redirect: false, callbackUrl: '/user' });
        push(url);
    };

    return (
        <div>
            <h3>Web3 Authentication</h3>
            <button onClick={handleAuth}>Authenticate via Metamask</button>
        </div>
    );
}

export default SignIn;
